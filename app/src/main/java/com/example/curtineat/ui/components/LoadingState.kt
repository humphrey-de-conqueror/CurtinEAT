package com.example.curtineat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingState(
	modifier: Modifier = Modifier,
	message: String? = null,
	fullScreen: Boolean = false
) {
	val containerModifier = if (fullScreen) {
		modifier.fillMaxSize()
	} else {
		modifier.fillMaxWidth()
	}

	Box(
		modifier = containerModifier.padding(24.dp),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			CircularProgressIndicator(
				modifier = Modifier.size(36.dp),
				color = MaterialTheme.colorScheme.primary,
				strokeWidth = 3.dp
			)

			if (!message.isNullOrBlank()) {
				Text(
					text = message,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}