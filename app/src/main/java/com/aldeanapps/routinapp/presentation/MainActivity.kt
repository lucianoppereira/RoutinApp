package com.aldeanapps.routinapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aldeanapps.routinapp.presentation.main.MainScreen
import com.aldeanapps.routinapp.presentation.common.theme.RoutinAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoutinAppTheme {
                MainScreen()
            }
        }
    }
}