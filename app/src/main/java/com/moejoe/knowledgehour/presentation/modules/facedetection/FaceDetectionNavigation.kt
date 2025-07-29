package com.moejoe.knowledgehour.presentation.modules.facedetection

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Created by manoj-20477 on 24/04/25.
 */

fun NavController.navigateToFaceDetectionScreen() {
    navigate(FaceDetectionScreen)
}
    
fun NavGraphBuilder.faceDetectionScreen() {
    composable<FaceDetectionScreen> {
        FaceDetectionRoute()
    }
}

@Serializable
object FaceDetectionScreen