package dev.csse.ctran156.recall

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import dev.csse.ctran156.recall.ui.RecallApp
import dev.csse.ctran156.recall.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                when (intent?.action) {
                    Intent.ACTION_SEND -> {
                        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                        if (text != null && text.startsWith("recall://")) {
                            navController.navigate(text.removePrefix("recall://"))
                        }
                    }

                    Intent.ACTION_VIEW -> {
                        navController.handleDeepLink(intent)
                    }
                }
            }

            AppTheme(dynamicColor = false) {
                RecallApp(navController = navController)
            }
        }
    }
}
