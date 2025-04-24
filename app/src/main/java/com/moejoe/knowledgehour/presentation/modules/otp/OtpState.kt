package com.moejoe.knowledgehour.presentation.modules.otp

/**
 * Created by manoj-20477 on 28/01/25.
 */
data class OtpState(
    val code: List<Int?> = emptyList(),
    val focusedIndex: Int? = 0,
    val isValid: Boolean? = null,
    val codeVersion: Int = 0
)
