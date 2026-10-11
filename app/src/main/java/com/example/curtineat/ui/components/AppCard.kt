package com.example.curtineat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun AppCard(
	modifier: Modifier = Modifier,
	containerColor: Color = MaterialTheme.colorScheme.surface,
	shape: Shape = RoundedCornerShape(20.dp),
	contentPadding: PaddingValues = PaddingValues(16.dp),
	onClick: (() -> Unit)? = null,
	enabled: Boolean = true,
	content: @Composable ColumnScope.() -> Unit
) {
	val colors = CardDefaults.cardColors(
		containerColor = containerColor
	)

	if (onClick != null) {
		Card(
			onClick = onClick,
			modifier = modifier,
			enabled = enabled,
			shape = shape,
			colors = colors
		) {
			Column(
				modifier = Modifier.padding(contentPadding),
				content = content
			)
		}
	} else {
		Card(
			modifier = modifier,
			shape = shape,
			colors = colors
		) {
			Column(
				modifier = Modifier.padding(contentPadding),
				content = content
			)
		}
	}
}