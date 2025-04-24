package com.moejoe.knowledgehour.presentation.modules.sharedelementtransition

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moejoe.knowledgehour.navigation.LocalSharedTransitionScope
import com.moejoe.knowledgehour.presentation.modules.Modules

/**
 * Created by manoj-20477 on 04/03/25.
 */

@Composable
fun SharedElementTransitionRoute(animatedContentScope: AnimatedContentScope) {
    SharedElementTransitionScreen(animatedContentScope = animatedContentScope)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementTransitionScreen(animatedContentScope: AnimatedContentScope) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    with(sharedTransitionScope) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = "bg_${Modules.SharedElementTransition.moduleName}"
                    ),
                    animatedVisibilityScope = animatedContentScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                ),
        ) {
            Image(
                painter = painterResource(id = Modules.SharedElementTransition.moduleLogo),
                contentDescription = Modules.SharedElementTransition.moduleName,
                modifier = Modifier
                    .size(120.dp)
                    .sharedElement(
                        state = sharedTransitionScope.rememberSharedContentState(key = "icon_${Modules.SharedElementTransition.moduleLogo}"),
                        animatedVisibilityScope = animatedContentScope
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = Modules.SharedElementTransition.moduleName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.sharedElement(
                    state = sharedTransitionScope.rememberSharedContentState(key = "name_${Modules.SharedElementTransition.moduleName}"),
                    animatedVisibilityScope = animatedContentScope)
                    .skipToLookaheadSize()
            )

            // Additional module details
        }
    }
}