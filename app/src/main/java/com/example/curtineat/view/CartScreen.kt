package com.example.curtineat.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel

@Composable
fun CartScreen(
    appViewModel: AppViewModel,
    onBackButtonClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    AppScaffold(
        onHomeClick = onHomeClick,
        title = "Order Summary",
        showSearch = false,
        showNotifications = true
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "This is cart screen")

            Button(
                onClick = onBackButtonClick
            ) {
                Text(text = "Back")
            }
        }
    }
}