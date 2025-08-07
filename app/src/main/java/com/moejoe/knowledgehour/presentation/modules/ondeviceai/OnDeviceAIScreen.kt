package com.moejoe.knowledgehour.presentation.modules.ondeviceai

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.generationConfig
import kotlinx.coroutines.launch

/**
 * Created by manoj-20477 on 29/07/25.
 */

@Composable
fun OnDeviceAIScreenRoute(onBackPressed: () -> Unit = {}) {
    OnDeviceAIScreen()
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OnDeviceAIScreen(modifier: Modifier = Modifier) {
    var text by rememberSaveable{mutableStateOf("")}
    var aiGeneratedText by rememberSaveable{mutableStateOf("")}
    val localContext = LocalContext.current

    val generationConfig = generationConfig {
        context = localContext
        temperature = 0.2f
        topK = 16
        maxOutputTokens = 256
    }

    val generativeModel = GenerativeModel(
        generationConfig = generationConfig
    )

    val scope = rememberCoroutineScope()

    scope.launch {
        val input = "I want you to act as an English proofreader. I will provide you " +
                "texts, and I would like you to review them for any spelling, grammar, or" +
                " punctuation errors. Once you have finished reviewing the text, provide me" +
                " with any necessary corrections or suggestions for improving the text: \n$text"
        val response = generativeModel.generateContent(input)
        aiGeneratedText = response.text ?: "Try Again"
        print(response.text)
    }

    Column {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
        )

        Text(aiGeneratedText)
    }
}