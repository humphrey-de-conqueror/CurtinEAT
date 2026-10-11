package com.example.curtineat.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBackButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	contentDescription: String = "Back"
) {
	IconButton(
		onClick = onClick,
		modifier = modifier
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Filled.ArrowBack,
			contentDescription = contentDescription,
			modifier = Modifier.size(24.dp)
		)
	}
}