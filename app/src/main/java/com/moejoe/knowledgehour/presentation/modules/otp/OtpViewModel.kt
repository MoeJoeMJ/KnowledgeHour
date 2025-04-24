package com.moejoe.knowledgehour.presentation.modules.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by manoj-20477 on 28/01/25.
 */
private const val VALID_OTP = "123456"

class OtpViewModel: ViewModel() {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    init {
        fetchOtpLength()
    }


    private fun fetchOtpLength() {
        viewModelScope.launch {
            val otpLength = fetchFromApi()
            _state.value = _state.value.copy(
                code = List(otpLength) { null }
            )
        }
    }

    private suspend fun fetchFromApi(): Int {
        delay(1000)
        return 6
    }

    fun onAction(action: OtpAction) {
        when(action) {
            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }
            is OtpAction.OnChangeFieldFocused -> {
                _state.update {
                    it.copy(focusedIndex = action.index)
                }
            }
            is OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)

                _state.update {
                    it.copy(
                        code = it.code.mapIndexed { index, number ->
                            if (index == previousIndex) null else number
                        },
                        focusedIndex = previousIndex,
                        codeVersion = it.codeVersion + 1
                    )
                }
            }

            is OtpAction.OnPasteOtp -> {
                pasteOtp(action.otp)
            }

        }
    }

    private var lastPastedText: String? = null
    private var lastPasteTime: Long = 0

    private fun pasteOtp(pastedOtp: String) {

        val currentTime = System.currentTimeMillis()

        if (pastedOtp == lastPastedText && currentTime - lastPasteTime < 500) {
            return
        }

        lastPastedText = pastedOtp
        lastPasteTime = currentTime

        val currentState = _state.value
        val focusedIndex = currentState.focusedIndex ?: 0
        val newDigits = pastedOtp.mapNotNull { it.toString().toIntOrNull() }

        if (newDigits.isEmpty()) return

        val updatedCode = state.value.code.toMutableList()

        var insertIndex = focusedIndex

        for (digit in newDigits) {
            if (insertIndex < updatedCode.size) {
                updatedCode[insertIndex] = digit
                insertIndex++
            } else {
                break
            }
        }


        viewModelScope.launch {
            delay(100)
            lastPastedText = null
        }

        val nextFocusIndex = updatedCode.indexOfFirst { it == null }.takeIf { it != -1 }

        val isValid = if (updatedCode.none { it == null }) {
            updatedCode.joinToString("") == VALID_OTP
        } else {
            null
        }

        _state.update {
            it.copy(
                code = updatedCode,
                focusedIndex = nextFocusIndex,
                isValid = isValid,
                codeVersion = it.codeVersion + 1
            )
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }

        val wasNumberRemoved = number == null

        _state.update {
            it.copy(code = newCode,
                focusedIndex = if(wasNumberRemoved || it.code.getOrNull(index) != null) {
                    it.focusedIndex
                } else {
                    getNextFocusedTextFieldIndex(
                        currentCode = it.code,
                        currentFocusedIndex = it.focusedIndex
                    )
                },
                isValid = if (newCode.none { it == null}) {
                    newCode.joinToString("") == VALID_OTP
                } else {
                    null
                },
                codeVersion = it.codeVersion + 1
            )
        }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if (currentFocusedIndex == null) return null

        if(currentFocusedIndex == currentCode.size - 1) return currentFocusedIndex

        return getFirstEmptyFieldIndexAfterFocusedIndex(currentCode, currentFocusedIndex)
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(code: List<Int?>, currentFocusedIndex: Int): Int {
        code.forEachIndexed { index, number ->
            if(index <= currentFocusedIndex) return@forEachIndexed

            if(number == null) return index
        }
        return currentFocusedIndex
    }
}