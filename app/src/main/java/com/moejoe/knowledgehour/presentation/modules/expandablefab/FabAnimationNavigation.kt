package com.moejoe.knowledgehour.presentation.modules.expandablefab

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val FabAnimationRoute = "fab_animation"

fun NavController.navigateToFabAnimationScreen() {
    navigate(FabAnimationRoute)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.fabAnimationScreen(navController: NavHostController) {
    composable(
        route = FabAnimationRoute,
        enterTransition = { slideInVertically { height -> height } },
        exitTransition = { slideOutVertically { height -> height } },
        popEnterTransition = { slideInVertically { height -> -height } },
        popExitTransition = { slideOutVertically { height -> -height } }
    ) {
        FabAnimationRoute(navController)
    }
}