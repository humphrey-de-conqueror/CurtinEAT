package com.example.curtineat.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole

@Composable
fun StartupScreen(
    appViewModel: AppViewModel,
    onRoleResolved: (UserRole) -> Unit
) {
    LaunchedEffect(Unit) {
        appViewModel.checkCurrentUserRole { role ->
            onRoleResolved(role)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}