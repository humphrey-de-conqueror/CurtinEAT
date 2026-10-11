package com.example.curtineat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(
	title: String,
	modifier: Modifier = Modifier,
	subtitle: String? = null,
	actionText: String? = null,
	onActionClick: (() -> Unit)? = null
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Column(
			modifier = Modifier.weight(1f)
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			if (!subtitle.isNullOrBlank()) {
				Text(
					text = subtitle,
					modifier = Modifier.padding(top = 4.dp),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}

		if (!actionText.isNullOrBlank() && onActionClick != null) {
			AppButton(
				text = actionText,
				onClick = onActionClick,
				style = AppButtonStyle.TEXT
			)
		}
	}
}
