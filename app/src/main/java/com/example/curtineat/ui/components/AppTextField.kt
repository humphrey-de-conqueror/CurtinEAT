package com.example.curtineat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	modifier: Modifier = Modifier,
	placeholder: String? = null,
	supportingText: String? = null,
	isError: Boolean = false,
	enabled: Boolean = true,
	readOnly: Boolean = false,
	singleLine: Boolean = true,
	minLines: Int = 1,
	maxLines: Int = if (singleLine) 1 else 5,
	keyboardType: KeyboardType = KeyboardType.Text,
	isPassword: Boolean = false
) {
	Column(
		modifier = modifier.fillMaxWidth()
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.Medium,
			color = if (isError) {
				MaterialTheme.colorScheme.error
			} else {
				MaterialTheme.colorScheme.onSurface
			},
			modifier = Modifier.padding(bottom = 8.dp)
		)

		OutlinedTextField(
			value = value,
			onValueChange = onValueChange,
			modifier = Modifier.fillMaxWidth(),
			placeholder = placeholder?.let {
				{
					Text(
						text = it,
						style = MaterialTheme.typography.bodyLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			},
			enabled = enabled,
			readOnly = readOnly,
			singleLine = singleLine,
			minLines = minLines,
			maxLines = maxLines,
			isError = isError,
			shape = RoundedCornerShape(14.dp),
			keyboardOptions = KeyboardOptions(
				keyboardType = if (isPassword) {
					KeyboardType.Password
				} else {
					keyboardType
				}
			),
			visualTransformation = if (isPassword) {
				PasswordVisualTransformation()
			} else {
				VisualTransformation.None
			},
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = MaterialTheme.colorScheme.primary,
				unfocusedBorderColor = MaterialTheme.colorScheme.outline,
				focusedLabelColor = MaterialTheme.colorScheme.primary,
				cursorColor = MaterialTheme.colorScheme.primary,
				errorBorderColor = MaterialTheme.colorScheme.error,
				errorLabelColor = MaterialTheme.colorScheme.error
			)
		)

		if (supportingText != null) {
			Text(
				text = supportingText,
				style = MaterialTheme.typography.bodySmall,
				color = if (isError) {
					MaterialTheme.colorScheme.error
				} else {
					MaterialTheme.colorScheme.onSurfaceVariant
				},
				modifier = Modifier.padding(
					start = 4.dp,
					top = 6.dp
				)
			)
		}
	}
}