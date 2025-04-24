package com.moejoe.knowledgehour.presentation.modules.otp

/**
 * Created by manoj-20477 on 28/01/25.
 */
sealed interface OtpAction {
    data class OnEnterNumber(val number: Int?, val index: Int) : OtpAction
    data class OnChangeFieldFocused(val index: Int) : OtpAction
    data object OnKeyboardBack: OtpAction
    data class OnPasteOtp(val otp: String): OtpAction
}