package com.moejoe.knowledgehour.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.moejoe.knowledgehour.R
import com.moejoe.knowledgehour.navigation.LocalSharedTransitionScope
import com.moejoe.knowledgehour.presentation.modules.Modules
import com.moejoe.knowledgehour.presentation.modules.facedetection.navigateToFaceDetectionScreen
import com.moejoe.knowledgehour.presentation.modules.expandablefab.navigateToFabAnimationScreen
import com.moejoe.knowledgehour.presentation.modules.otp.navigateToOtpScreen
import com.moejoe.knowledgehour.presentation.modules.sharedelementtransition.navigateToSharedElementTransitionScreen

/**
 * Created by manoj-20477 on 12/12/24.
 */

@Composable
fun HomeRoute(navController: NavHostController, animatedContentScope: AnimatedContentScope) {

    HomeScreen(animatedContentScope = animatedContentScope) { moduleName ->
        when(moduleName) {
            Modules.OTPModule.moduleName -> {
                navController.navigateToOtpScreen()
            }

            Modules.SharedElementTransition.moduleName -> {
                navController.navigateToSharedElementTransitionScreen()
            }

            Modules.FaceDetection.moduleName -> {
                navController.navigateToFaceDetectionScreen()
            }

            Modules.FabAnimation.moduleName -> {
                navController.navigateToFabAnimationScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(animatedContentScope: AnimatedContentScope, onModuleClicked: (String) -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.top_bar_title_knowledge_hour))
                },
                windowInsets = TopAppBarDefaults.windowInsets.add(
                    WindowInsets(
                    top = 8.dp
                )
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            itemsIndexed(Modules.entries) { index, module ->
                GridItem(module.moduleName, moduleLogo = module.moduleLogo, onModuleClicked, animatedContentScope = animatedContentScope)
            }
        }

    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GridItem(
    moduleName: String,
    moduleLogo: Int,
    onModuleClicked: (String) -> Unit,
    animatedContentScope: AnimatedContentScope,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    with(sharedTransitionScope) {
        Card(
            modifier = Modifier
                .height(100.dp)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = "bg_$moduleName"
                    ),
                    animatedVisibilityScope = animatedContentScope
                ),
            colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            onClick = {
                onModuleClicked(moduleName)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(moduleLogo),
                    contentDescription = null,
                    modifier = Modifier.sharedElement(
                        state = sharedTransitionScope.rememberSharedContentState(key = "icon_${moduleLogo}"),
                        animatedVisibilityScope = animatedContentScope)
                )
                Text(text = moduleName, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .sharedElement(
                        state = sharedTransitionScope.rememberSharedContentState(key = "name_${moduleName}"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .skipToLookaheadSize()
                )
            }
        }
    }
}



@Preview
@PreviewScreenSizes
@Composable
private fun HomeScreenPreview() {


//    HomeScreen(
//        onModuleClicked = {},
//        animatedContentScope = MockContentScope()
//    )
}

