package com.example.curtineat.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class AppButtonStyle {
	PRIMARY,
	SECONDARY,
	OUTLINED,
	TEXT,
	DESTRUCTIVE
}

@Composable
fun AppButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	style: AppButtonStyle = AppButtonStyle.PRIMARY,
	enabled: Boolean = true,
	fullWidth: Boolean = false
) {
	val buttonModifier = if (fullWidth) {
		modifier.fillMaxWidth()
	} else {
		modifier
	}

	val shape = RoundedCornerShape(14.dp)

	when (style) {
		AppButtonStyle.PRIMARY -> {
			Button(
				onClick = onClick,
				modifier = buttonModifier,
				enabled = enabled,
				shape = shape,
				contentPadding = PaddingValues(
					horizontal = 20.dp,
					vertical = 12.dp
				)
			) {
				ButtonLabel(text)
			}
		}

		AppButtonStyle.SECONDARY -> {
			Button(
				onClick = onClick,
				modifier = buttonModifier,
				enabled = enabled,
				shape = shape,
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.secondaryContainer,
					contentColor = MaterialTheme.colorScheme.onSecondaryContainer
				),
				contentPadding = PaddingValues(
					horizontal = 20.dp,
					vertical = 12.dp
				)
			) {
				ButtonLabel(text)
			}
		}

		AppButtonStyle.OUTLINED -> {
			OutlinedButton(
				onClick = onClick,
				modifier = buttonModifier,
				enabled = enabled,
				shape = shape,
				contentPadding = PaddingValues(
					horizontal = 20.dp,
					vertical = 12.dp
				)
			) {
				ButtonLabel(text)
			}
		}

		AppButtonStyle.TEXT -> {
			TextButton(
				onClick = onClick,
				modifier = buttonModifier,
				enabled = enabled
			) {
				ButtonLabel(text)
			}
		}

		AppButtonStyle.DESTRUCTIVE -> {
			Button(
				onClick = onClick,
				modifier = buttonModifier,
				enabled = enabled,
				shape = shape,
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.error,
					contentColor = MaterialTheme.colorScheme.onError
				),
				contentPadding = PaddingValues(
					horizontal = 20.dp,
					vertical = 12.dp
				)
			) {
				ButtonLabel(text)
			}
		}
	}
}

@Composable
private fun ButtonLabel(text: String) {
	Text(
		text = text,
		style = MaterialTheme.typography.labelLarge,
		fontWeight = FontWeight.SemiBold
	)
}