package com.moejoe.knowledgehour.presentation.modules.ondeviceai

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Created by manoj-20477 on 29/07/25.
 */
    
fun NavController.navigateToOnDeviceAI() {
    navigate(OnDeviceAIScreen)
}

fun NavGraphBuilder.onDeviceAIScreen(onBackPressed: () -> Unit) {
    composable<OnDeviceAIScreen> {
        OnDeviceAIScreenRoute(onBackPressed = onBackPressed)
    }
}

@Serializable
object OnDeviceAIScreen