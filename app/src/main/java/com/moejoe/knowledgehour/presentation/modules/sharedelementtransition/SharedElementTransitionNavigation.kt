package com.moejoe.knowledgehour.presentation.modules.sharedelementtransition

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Created by manoj-20477 on 04/03/25.
 */

fun NavController.navigateToSharedElementTransitionScreen() {
    navigate(SharedElementTransitionScreen)
}


fun NavGraphBuilder.sharedElementTransitionScreen() {
    composable<SharedElementTransitionScreen> {
        SharedElementTransitionRoute(animatedContentScope = this)
    }
}

@Serializable
object SharedElementTransitionScreen