//package com.example.curtineat.ui.components
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun AppCard(
//	modifier: Modifier = Modifier,
//	containerColor: Color = MaterialTheme.colorScheme.surface,
//	shape: Shape = RoundedCornerShape(20.dp),
//	contentPadding: PaddingValues = PaddingValues(16.dp),
//	onClick: (() -> Unit)? = null,
//	enabled: Boolean = true,
//	content: @Composable ColumnScope.() -> Unit
//) {
//	val colors = CardDefaults.cardColors(
//		containerColor = containerColor
//	)
//
//	if (onClick != null) {
//		Card(
//			onClick = onClick,
//			modifier = modifier,
//			enabled = enabled,
//			shape = shape,
//			colors = colors
//		) {
//			Column(
//				modifier = Modifier.padding(contentPadding),
//				content = content
//			)
//		}
//	} else {
//		Card(
//			modifier = modifier,
//			shape = shape,
//			colors = colors
//		) {
//			Column(
//				modifier = Modifier.padding(contentPadding),
//				content = content
//			)
//		}
//	}
//}

package com.example.curtineat.ui.components

import androidx.compose.foundation.BorderStroke
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
	containerColor: Color =
		MaterialTheme.colorScheme.surface,
	shape: Shape =
		RoundedCornerShape(20.dp),
	contentPadding: PaddingValues =
		PaddingValues(16.dp),
	onClick: (() -> Unit)? = null,
	enabled: Boolean = true,
	content: @Composable ColumnScope.() -> Unit
) {
	val colors = CardDefaults.cardColors(
		containerColor = containerColor
	)

	val elevation = CardDefaults.cardElevation(
		defaultElevation = 1.dp,
		pressedElevation = 1.dp,
		focusedElevation = 2.dp,
		hoveredElevation = 2.dp,
		disabledElevation = 0.dp
	)

	val border = BorderStroke(
		width = 1.dp,
		color = MaterialTheme.colorScheme
			.outlineVariant
			.copy(alpha = 0.45f)
	)

	if (onClick != null) {
		Card(
			onClick = onClick,
			modifier = modifier,
			enabled = enabled,
			shape = shape,
			colors = colors,
			elevation = elevation,
			border = border
		) {
			Column(
				modifier = Modifier.padding(
					contentPadding
				),
				content = content
			)
		}
	} else {
		Card(
			modifier = modifier,
			shape = shape,
			colors = colors,
			elevation = elevation,
			border = border
		) {
			Column(
				modifier = Modifier.padding(
					contentPadding
				),
				content = content
			)
		}
	}
}