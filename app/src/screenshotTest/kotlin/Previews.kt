import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.moejoe.knowledgehour.presentation.modules.otp.OtpScreen
import com.moejoe.knowledgehour.presentation.modules.otp.OtpState

/**
 * Created by manoj-20477 on 24/03/25.
 */


@Preview
@Composable
private fun OtpScreenPreview() {
    OtpScreen(
        onBackClick = {},
        focusRequesters = listOf(
            FocusRequester(), FocusRequester(),
            FocusRequester(), FocusRequester()
        ),
        state = OtpState(
            code = listOf(1, 3, 4, 6),
            focusedIndex = 0,
            isValid = false
        ),
        onAction = {},
        modifier = Modifier
    )
}