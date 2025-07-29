package com.moejoe.knowledgehour.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.moejoe.knowledgehour.presentation.HomeScreen
import com.moejoe.knowledgehour.presentation.homeScreen

/**
 * Created by manoj-20477 on 24/01/25.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope> { error("No SharedTransitionScope provided.") }
//val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope> { error("No AnimatedVisibilityScope provided.") }


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)
@Composable
fun KnowledgeHourNavHost(modifier: Modifier = Modifier) {


    SharedTransitionLayout {

        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {

            val navController = rememberNavController()
//            AnimatedContent(
//                targetState = navController.currentBackStackEntry?.destination?.route
//            ) {
//                CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                    NavHost(
                        navController = navController,
                        startDestination = HomeScreen
                    ) {
                        homeScreen(navController = navController)
                    }
//                }
//            }
        }
    }
}
