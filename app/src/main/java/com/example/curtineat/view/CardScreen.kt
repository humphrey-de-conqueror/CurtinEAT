package com.example.curtineat.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CardScreen(onBackButtonClick: () -> Unit) {
    Column() {
        Text(text = "this is card screen")
        Button(
            onClick = { onBackButtonClick() }
        ) {
            Text(text = "back")
        }
    }
}