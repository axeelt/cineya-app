package com.axeelt.cineya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.axeelt.cineya.navigation.AppNavigation
import com.axeelt.cineya.ui.theme.CineYaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            CineYaTheme {

                AppNavigation()
            }
        }
    }
}