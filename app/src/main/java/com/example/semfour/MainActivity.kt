package com.example.semfour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.semfour.ui.SemFourApp
import com.example.semfour.ui.theme.SemFourTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal de SemFour.
 * Hilt inyecta dependencias via @AndroidEntryPoint.
 * La UI completa es Jetpack Compose dentro de [SemFourTheme].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SemFourTheme {
                SemFourApp()
            }
        }
    }
}