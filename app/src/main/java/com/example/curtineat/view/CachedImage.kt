
package com.example.curtineat.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.state.ImageUiState

@Composable
fun CachedImage(
	imageId: String,
	viewModel: AppViewModel,
	contentDescription: String?,
	modifier: Modifier = Modifier,
	retryOnTap: Boolean = false,
	placeholderIcon: ImageVector = Icons.Default.AccountCircle
) {
	val imageStates by viewModel.imageStates.collectAsState()

	val imageState = imageStates[imageId]

	LaunchedEffect(imageId) {
		if (imageId.isNotBlank()) {
			viewModel.loadImage(imageId)
		}
	}

	val bitmap = remember(imageState) {
		val bytes = (imageState as? ImageUiState.Loaded)?.bytes

		if (bytes == null) {
			null
		} else {
			BitmapFactory.decodeByteArray(
				bytes,
				0,
				bytes.size
			)?.asImageBitmap()
		}
	}

	val canRetry =
		imageState is ImageUiState.Unavailable ||
			(imageState is ImageUiState.Loaded && bitmap == null)

	Box(
		modifier = if (canRetry) {
			modifier.clickable { viewModel.retryImage(imageId) }
		} else {
			modifier
		}
	) {
		if (bitmap != null) {
			Image(
				bitmap = bitmap,
				contentDescription = contentDescription,
				modifier = Modifier.fillMaxSize(),
				contentScale = ContentScale.Crop
			)
		} else {
			ImagePlaceholder(
				isLoading = imageId.isNotBlank() &&
						(imageState == null || imageState is ImageUiState.Loading),
				icon = placeholderIcon,
				contentDescription = contentDescription
			)
		}
	}
}

@Composable
fun ImagePlaceholder(
	isLoading: Boolean,
	icon: ImageVector,
	contentDescription: String?,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.surfaceVariant),
		contentAlignment = Alignment.Center
	) {
		if (isLoading) {
			CircularProgressIndicator(
				modifier = Modifier.size(24.dp),
				strokeWidth = 2.dp
			)
		} else {
			Icon(
				imageVector = icon,
				contentDescription = contentDescription,
				modifier = Modifier.size(40.dp),
				tint = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}
