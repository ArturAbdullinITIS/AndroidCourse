package ru.itis.practice.presentation.screen.mainscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MainScreen() {
    Text(
        modifier = Modifier.padding(32.dp),
        text = "Main Screen",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )
}