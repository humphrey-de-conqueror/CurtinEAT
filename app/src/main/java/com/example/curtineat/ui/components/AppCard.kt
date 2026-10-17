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
//import androidx.compose.ui.draw.dropShadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.graphics.shadow.Shadow
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.DpOffset
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
//
//	// Optional shadow settings
//	showShadow: Boolean = false,
//	shadowRadius: Dp = 4.dp,
//	shadowSpread: Dp = 0.dp,
//	shadowColor: Color = Color.Black.copy(alpha = 0.20f),
//	shadowOffset: DpOffset = DpOffset(0.dp, 2.dp),
//
//	content: @Composable ColumnScope.() -> Unit
//) {
//	val colors = CardDefaults.cardColors(
//		containerColor = containerColor
//	)
//
//	val elevation = CardDefaults.cardElevation(
//		defaultElevation = 0.dp,
//		pressedElevation = 0.dp,
//		focusedElevation = 0.dp,
//		hoveredElevation = 0.dp,
//		disabledElevation = 0.dp
//	)
//
//	val cardModifier = if (showShadow) {
//		modifier.dropShadow(
//			shape = shape,
//			shadow = Shadow(
//				radius = shadowRadius,
//				spread = shadowSpread,
//				color = shadowColor,
//				offset = shadowOffset
//			)
//		)
//	} else {
//		modifier
//	}
//
//	if (onClick != null) {
//		Card(
//			onClick = onClick,
//			modifier = cardModifier,
//			enabled = enabled,
//			shape = shape,
//			colors = colors,
//			elevation = elevation,
//			border = null
//		) {
//			Column(
//				modifier = Modifier.padding(contentPadding),
//				content = content
//			)
//		}
//	} else {
//		Card(
//			modifier = cardModifier,
//			shape = shape,
//			colors = colors,
//			elevation = elevation,
//			border = null
//		) {
//			Column(
//				modifier = Modifier.padding(contentPadding),
//				content = content
//			)
//		}
//	}
//}

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppCard(
	modifier: Modifier = Modifier,
	containerColor: Color = MaterialTheme.colorScheme.surface,
	shape: Shape = RoundedCornerShape(20.dp),
	contentPadding: PaddingValues = PaddingValues(16.dp),
	onClick: (() -> Unit)? = null,
	enabled: Boolean = true,
	showShadow: Boolean = false,
	shadowElevation: Dp = 4.dp,
	content: @Composable ColumnScope.() -> Unit
) {
	val colors = CardDefaults.cardColors(
		containerColor = containerColor
	)

	val elevation = CardDefaults.cardElevation(
		defaultElevation = if (showShadow) shadowElevation else 0.dp,
		pressedElevation = if (showShadow) 2.dp else 0.dp,
		focusedElevation = if (showShadow) shadowElevation else 0.dp,
		hoveredElevation = if (showShadow) shadowElevation else 0.dp,
		disabledElevation = 0.dp
	)

	if (onClick != null) {
		Card(
			onClick = onClick,
			modifier = modifier,
			enabled = enabled,
			shape = shape,
			colors = colors,
			elevation = elevation
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
			colors = colors,
			elevation = elevation
		) {
			Column(
				modifier = Modifier.padding(contentPadding),
				content = content
			)
		}
	}
}