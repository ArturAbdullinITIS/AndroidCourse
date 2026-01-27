package ru.itis.practice.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.practice.presentation.screens.mainscreen.MainScreen
import ru.itis.practice.presentation.screens.mainscreen.MainViewModel
import ru.itis.practice.ui.theme.PracticeTheme


@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                MainScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(!viewModel.state.value.backgroundLaunch) {
            viewModel.restartCoroutines()
        }

    }

    override fun onStop() {
        super.onStop()
        if(!viewModel.state.value.backgroundLaunch) {
            viewModel.stopCoroutines()
        }

    }
}
