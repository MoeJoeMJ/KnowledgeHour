package com.moejoe.knowledgehour.presentation.modules.facedetection

import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.Executor

/**
 * Created by manoj-20477 on 23/04/25.
 */

data class FaceData(
    val faceEmbedding: FloatArray,
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FaceData
        if (!faceEmbedding.contentEquals(other.faceEmbedding)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = faceEmbedding.contentHashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}

// Simple in-memory database for face data
object FaceDatabase {
    private val faces = mutableListOf<FaceData>()

    fun addFace(faceData: FaceData) {
        faces.add(faceData)
    }

    fun findMatchingFace(embedding: FloatArray, similarityThreshold: Float = 0.8f): FaceData? {
        if (faces.isEmpty()) return null
        
        // In a real app, you'd use proper face recognition algorithms here
        // This is a very simplistic approach (using dot product as similarity)
        return faces.maxByOrNull { faceData ->
            calculateSimilarity(embedding, faceData.faceEmbedding)
        }?.let { bestMatch ->
            val similarity = calculateSimilarity(embedding, bestMatch.faceEmbedding)
            if (similarity > similarityThreshold) bestMatch else null
        }
    }

    private fun calculateSimilarity(embedding1: FloatArray, embedding2: FloatArray): Float {
        // Simple dot product similarity (cosine similarity without normalization)
        var similarity = 0f
        val minLength = minOf(embedding1.size, embedding2.size)
        for (i in 0 until minLength) {
            similarity += embedding1[i] * embedding2[i]
        }
        return similarity
    }
}

@Composable
fun FaceDetectionRoute() {
    FaceDetectionScreen()
}

@Composable
fun FaceDetectionScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { ContextCompat.getMainExecutor(context) }
    
    // Camera and preview state
    val previewView = remember { PreviewView(context) }
    var faceDetected by remember { mutableStateOf(false) }
    var currentFace by remember { mutableStateOf<Face?>(null) }
    var statusText by remember { mutableStateOf("Scanning...") }
    
    // Capture and recognition state
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var capturedFaceEmbedding by remember { mutableStateOf<FloatArray?>(null) }
    var isFaceCaptured by remember { mutableStateOf(false) }
    var recognizedName by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf("") }
    var showNameInput by remember { mutableStateOf(false) }
    
    // Camera setup
    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val captureOptions = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()
            imageCapture = captureOptions

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(executor) { imageProxy ->
                processImageProxy(imageProxy) { hasFace, face ->
                    faceDetected = hasFace
                    currentFace = face
                    statusText = if (hasFace) "Face Detected" else "No Face Detected"
                    
                    // Check for recognized faces when not capturing
                    if (hasFace && !isFaceCaptured && face != null) {
                        val faceEmbedding = extractFaceEmbedding(face)
                        val matchingFace = FaceDatabase.findMatchingFace(faceEmbedding)
                        if (matchingFace != null) {
                            statusText = "Welcome back, ${matchingFace.name}!"
                            recognizedName = matchingFace.name
                        } else {
                            recognizedName = null
                        }
                    }
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis,
                captureOptions
            )
        }, executor)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera preview
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        
        // Status text
        Text(
            text = statusText,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
        
        // Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            if (showNameInput) {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            capturedFaceEmbedding?.let { embedding ->
                                FaceDatabase.addFace(FaceData(embedding, userName))
                                statusText = "Face saved as $userName"
                                showNameInput = false
                                isFaceCaptured = false
                                capturedFaceEmbedding = null
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                    
                    Spacer(modifier = Modifier.weight(0.1f))
                    
                    Button(
                        onClick = {
                            showNameInput = false
                            isFaceCaptured = false
                            capturedFaceEmbedding = null
                            statusText = "Scanning..."
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                }
            } else {
                Button(
                    onClick = {
                        if (faceDetected) {
                            captureFace(
                                imageCapture = imageCapture,
                                executor = executor,
                                onFaceCaptured = { face ->
                                    val embedding = extractFaceEmbedding(face)
                                    capturedFaceEmbedding = embedding
                                    isFaceCaptured = true
                                    
                                    // Check if the face is already known
                                    val matchingFace = FaceDatabase.findMatchingFace(embedding)
                                    if (matchingFace != null) {
                                        statusText = "Welcome back, ${matchingFace.name}!"
                                        recognizedName = matchingFace.name
                                    } else {
                                        statusText = "New face captured! Please enter your name."
                                        showNameInput = true
                                        userName = recognizedName ?: ""
                                    }
                                },
                                onError = {
                                    statusText = "Failed to capture face: ${it.message}"
                                }
                            )
                        } else {
                            statusText = "No face detected. Please position your face in the frame."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (recognizedName != null) "Verify as $recognizedName" else "Capture Face")
                }
            }
        }
    }
    
    // Clean up resources
    DisposableEffect(Unit) {
        onDispose {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }
    }
}

@OptIn(ExperimentalGetImage::class)
fun processImageProxy(
    imageProxy: ImageProxy,
    onResult: (Boolean, Face?) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        val detector = FaceDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { faces ->
                val hasFaces = faces.isNotEmpty()
                onResult(hasFaces, faces.firstOrNull())
                imageProxy.close()
            }
            .addOnFailureListener {
                onResult(false, null)
                imageProxy.close()
            }
    } else {
        imageProxy.close()
        onResult(false, null)
    }
}

fun captureFace(
    imageCapture: ImageCapture?,
    executor: Executor,
    onFaceCaptured: (Face) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    imageCapture?.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
        @OptIn(ExperimentalGetImage::class)
        override fun onCaptureSuccess(image: ImageProxy) {
            val mediaImage = image.image
            if (mediaImage != null) {
                val inputImage = InputImage.fromMediaImage(
                    mediaImage,
                    image.imageInfo.rotationDegrees
                )
                
                val options = FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                    .build()
                
                val detector = FaceDetection.getClient(options)
                
                detector.process(inputImage)
                    .addOnSuccessListener { faces ->
                        if (faces.isNotEmpty()) {
                            onFaceCaptured(faces[0])
                        }
                        image.close()
                    }
                    .addOnFailureListener {
                        image.close()
                    }
            } else {
                image.close()
            }
        }

        override fun onError(exception: ImageCaptureException) {
            onError(exception)
        }
    })
}

// Extract facial features as an embedding
// In a real app, you'd use a proper face embedding extractor like FaceNet
fun extractFaceEmbedding(face: Face): FloatArray {
    // This is a simplified mock implementation
    // In a real app, you would extract actual face embeddings using ML models
    val embedding = FloatArray(128) // Typical face embedding size
    
    // Use face landmarks, contours, tracking ID and other features
    // to create a unique signature for this face
    face.boundingBox.let {
        embedding[0] = it.width().toFloat()
        embedding[1] = it.height().toFloat()
        embedding[2] = it.exactCenterX()
        embedding[3] = it.exactCenterY()
    }
    
    face.allLandmarks.forEachIndexed { index, landmark ->
        val pos = index * 2
        if (pos < embedding.size - 1) {
            embedding[pos] = landmark.position.x
            embedding[pos + 1] = landmark.position.y
        }
    }
    
    // Add some classification values
    val classificationOffset = 100
    embedding[classificationOffset] = face.smilingProbability ?: 0f
    embedding[classificationOffset + 1] = face.rightEyeOpenProbability ?: 0f
    embedding[classificationOffset + 2] = face.leftEyeOpenProbability ?: 0f
    
    return embedding
}
