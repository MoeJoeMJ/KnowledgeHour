package com.moejoe.knowledgehour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.moejoe.knowledgehour.navigation.KnowledgeHourNavHost
import com.moejoe.knowledgehour.ui.theme.KnowledgeHourTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KnowledgeHourTheme {
                Surface {
                    KnowledgeHourNavHost()
                }
            }
        }
    }
}