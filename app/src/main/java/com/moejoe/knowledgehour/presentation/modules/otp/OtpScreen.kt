package com.moejoe.knowledgehour.presentation.modules.otp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Created by manoj-20477 on 24/01/25.
 */

@Composable
fun OtpRoute(onBackClick: () -> Unit) {

    val viewModel = viewModel<OtpViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequesters = remember(state.code.size) {
        List(state.code.size) { FocusRequester() }
    }

    val focusManager = LocalFocusManager.current
    val keyBoardManager = LocalSoftwareKeyboardController.current
    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code) {
        val allNumbersEntered = state.code.none { it == null }

        if(allNumbersEntered) {
            focusRequesters.forEach { it.freeFocus() }
            focusManager.clearFocus()
            keyBoardManager?.hide()
        }
    }
    OtpScreen(
        onBackClick = onBackClick,
        state = state,
        focusRequesters = focusRequesters,
        onAction = { action ->
            when(action) {
                is OtpAction.OnEnterNumber -> {
                    if(action.number != null) {
                        focusRequesters[action.index].freeFocus()
                    }
                }
                 else -> Unit
            }
            viewModel.onAction(action)
        },
        modifier = Modifier.size(100.dp)
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OtpScreen(
    onBackClick: () -> Unit,
    focusRequesters: List<FocusRequester>,
    state: OtpState,
    onAction: (OtpAction) -> Unit,
    modifier: Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                state.code.forEachIndexed { index, number ->
                    OtpInputField(
                        number = number,
                        otpLength = state.code.size,
                        focusRequester = focusRequesters[index],
                        onFocusChanged = { isFocused ->
                            if(isFocused) {
                                onAction(OtpAction.OnChangeFieldFocused(index))
                            }
                        },
                        onNumberChanged = { newNumber ->
                            onAction(OtpAction.OnEnterNumber(newNumber, index))
                        },
                        onKeyboardBack = {
                            onAction(OtpAction.OnKeyboardBack)
                        },
                        onPaste = {
                            onAction(
                                if (state.code[state.focusedIndex ?: 0] == null) {
                                    OtpAction.OnPasteOtp(it)
                                } else {
                                    OtpAction.OnPasteOtp(it.drop(1))
                                }
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }

                }
            state.isValid?.let { isValid ->
                Text(
                    text = if(isValid) "Otp is valid!" else "Otp is invalid!",
                    color = if(isValid) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun OtpScreenPreview() {
    OtpScreen(
        onBackClick = {},
        focusRequesters = listOf(FocusRequester(), FocusRequester(),
            FocusRequester(), FocusRequester()),
        state = OtpState(
            code = listOf(1, 3, 4, 5),
            focusedIndex = 0,
            isValid = false
        ),
        onAction = {},
        modifier = Modifier
    )
}
