package com.example.curtineat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

@Composable
fun AppBox(
	modifier: Modifier = Modifier,
	contentAlignment: Alignment = Alignment.TopStart,
	propagateMinConstraints: Boolean = false,
	containerColor: Color = Color.Transparent,
	shape: Shape = RectangleShape,
	contentColor: Color = contentColorFor(containerColor),
	content: @Composable BoxScope.() -> Unit
) {
	CompositionLocalProvider(LocalContentColor provides contentColor) {
		Box(
			modifier = modifier.background(containerColor, shape),
			contentAlignment = contentAlignment,
			propagateMinConstraints = propagateMinConstraints,
			content = content
		)
	}
}