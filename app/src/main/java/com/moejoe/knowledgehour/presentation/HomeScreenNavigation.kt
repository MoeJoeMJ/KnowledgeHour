package com.moejoe.knowledgehour.presentation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moejoe.knowledgehour.presentation.modules.expandablefab.fabAnimationScreen
import com.moejoe.knowledgehour.presentation.modules.facedetection.faceDetectionScreen
import com.moejoe.knowledgehour.presentation.modules.ondeviceai.onDeviceAIScreen
import com.moejoe.knowledgehour.presentation.modules.otp.otpScreen
import com.moejoe.knowledgehour.presentation.modules.sharedelementtransition.sharedElementTransitionScreen
import kotlinx.serialization.Serializable

/**
 * Created by manoj-20477 on 24/01/25.
 */

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeScreen(
    navController: NavHostController
) {
    composable<HomeScreen> {
        HomeRoute(navController, this)
    }

    otpScreen(onBackClick = navController::navigateUp)
    sharedElementTransitionScreen()
    faceDetectionScreen()
    fabAnimationScreen(navController = navController)
    onDeviceAIScreen(onBackPressed = navController::navigateUp)
}

@Serializable
object HomeScreen