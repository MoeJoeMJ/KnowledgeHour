package com.moejoe.knowledgehour.presentation.modules.expandablefab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moejoe.knowledgehour.ui.components.ExpandableFab
import com.moejoe.knowledgehour.ui.components.FabItem
import com.moejoe.knowledgehour.ui.theme.KnowledgeHourTheme

@Composable
fun FabAnimationRoute(navController: NavController) {
    ExpandableFabDemoScreen(
        onEditClicked = { /* Handle edit action */ },
        onEmailClicked = { /* Handle email action */ },
        onPhoneClicked = { /* Handle phone action */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableFabDemoScreen(
    onEditClicked: () -> Unit = {},
    onEmailClicked: () -> Unit = {},
    onPhoneClicked: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expandable FAB Demo") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Tap the FAB to see it expand!")

            // Expandable FAB positioned at bottom-end
            ExpandableFab(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                fabItems = listOf(
                    FabItem(
                        icon = Icons.Default.Edit,
                        label = "Edit",
                        onFabItemClicked = onEditClicked
                    ),
                    FabItem(
                        icon = Icons.Default.Email,
                        label = "Email",
                        onFabItemClicked = onEmailClicked
                    ),
                    FabItem(
                        icon = Icons.Default.Phone,
                        label = "Call",
                        onFabItemClicked = onPhoneClicked
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableFabDemoScreenPreview() {
    KnowledgeHourTheme {
        ExpandableFabDemoScreen()
    }
}