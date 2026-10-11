package com.example.curtineat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDialog(
	title: String,
	message: String,
	onConfirm: () -> Unit,
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier,
	confirmText: String = "Confirm",
	dismissText: String = "Cancel",
	destructive: Boolean = false
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		modifier = modifier,
		title = {
			Text(
				text = title,
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface
			)
		},
		text = {
			Text(
				text = message,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		},
		confirmButton = {
			TextButton(
				onClick = onConfirm
			) {
				Text(
					text = confirmText,
					color = if (destructive) {
						MaterialTheme.colorScheme.error
					} else {
						MaterialTheme.colorScheme.primary
					}
				)
			}
		},
		dismissButton = {
			TextButton(
				onClick = onDismiss
			) {
				Text(dismissText)
			}
		},
		shape = MaterialTheme.shapes.extraLarge,
		containerColor = MaterialTheme.colorScheme.surface
	)
}