package com.moejoe.knowledgehour.presentation.modules.otp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Created by manoj-20477 on 24/01/25.
 */

fun NavController.navigateToOtpScreen() {
    navigate(OtpScreen)
}

fun NavGraphBuilder.otpScreen(onBackClick: () -> Unit) {
    composable<OtpScreen> {
        OtpRoute(onBackClick)
    }
}

@Serializable
object OtpScreen