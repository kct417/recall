package dev.csse.ctran156.recall

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.csse.ctran156.recall.ui.RecallApp
import dev.csse.ctran156.recall.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedSubject = intent?.getStringExtra(Intent.EXTRA_SUBJECT)
        val sharedText = intent?.getStringExtra(Intent.EXTRA_TEXT)

        setContent {
            AppTheme(dynamicColor = false) {
                RecallApp(sharedSubject = sharedSubject, sharedText = sharedText)
            }
        }
    }
}
