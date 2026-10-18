package com.example.curtineat.view

import android.graphics.BitmapFactory
import android.util.LruCache
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.state.ImageUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Process-wide cache of DECODED (downsampled) bitmaps.
 * Survives items being disposed by LazyColumn/LazyRow, so scrolling back is instant.
 * Size is measured in KB, capped at 1/8 of the app's max heap.
 */
private val bitmapCache = object : LruCache<String, ImageBitmap>(
	(Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()
) {
	override fun sizeOf(key: String, value: ImageBitmap): Int =
		(value.width * value.height * 4 / 1024).coerceAtLeast(1)
}

/**
 * Decodes [bytes] at a reduced size so the result is at least [targetPx]
 * on both sides (when the source is that large). Must be called off the main thread.
 */
private fun decodeSampled(bytes: ByteArray, targetPx: Int): ImageBitmap? {
	return try {
		val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
		BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)

		if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

		var sample = 1
		while (
			bounds.outWidth / (sample * 2) >= targetPx &&
			bounds.outHeight / (sample * 2) >= targetPx
		) {
			sample *= 2
		}

		val options = BitmapFactory.Options().apply { inSampleSize = sample }

		BitmapFactory
			.decodeByteArray(bytes, 0, bytes.size, options)
			?.asImageBitmap()
	} catch (exception: Exception) {
		null
	}
}

@Composable
fun CachedImage(
	imageId: String,
	viewModel: AppViewModel,
	contentDescription: String?,
	modifier: Modifier = Modifier,
	retryOnTap: Boolean = false,
	placeholderIcon: ImageVector = Icons.Default.AccountCircle,
	targetPx: Int = 512
) {
	// Observe only THIS image's state, so one image finishing
	// does not recompose every other visible CachedImage.
	val imageState by remember(imageId, viewModel) {
		viewModel.imageStates
			.map { states -> states[imageId] }
			.distinctUntilChanged()
	}.collectAsState(initial = viewModel.imageStates.value[imageId])

	LaunchedEffect(imageId) {
		if (imageId.isNotBlank()) {
			viewModel.loadImage(imageId)
		}
	}

	val cacheKey = "$imageId@$targetPx"

	var decodeFailed by remember(imageState, cacheKey) { mutableStateOf(false) }

	// Instant on scroll-back (cache hit); otherwise decoded on a background thread.
	val bitmap by produceState<ImageBitmap?>(
		initialValue = bitmapCache.get(cacheKey),
		key1 = imageState,
		key2 = cacheKey
	) {
		val bytes = (imageState as? ImageUiState.Loaded)?.bytes

		if (value == null && bytes != null) {
			val decoded = withContext(Dispatchers.Default) {
				decodeSampled(bytes, targetPx)
			}

			if (decoded != null) {
				bitmapCache.put(cacheKey, decoded)
				value = decoded
			} else {
				decodeFailed = true
			}
		}
	}

	val canRetry =
		imageState is ImageUiState.Unavailable ||
			(imageState is ImageUiState.Loaded && decodeFailed)

	val isDecoding =
		imageState is ImageUiState.Loaded && bitmap == null && !decodeFailed

	Box(
		modifier = if (canRetry) {
			modifier.clickable { viewModel.retryImage(imageId) }
		} else {
			modifier
		}
	) {
		val currentBitmap = bitmap

		if (currentBitmap != null) {
			Image(
				bitmap = currentBitmap,
				contentDescription = contentDescription,
				modifier = Modifier.fillMaxSize(),
				contentScale = ContentScale.Crop
			)
		} else {
			ImagePlaceholder(
				isLoading = imageId.isNotBlank() &&
					(imageState == null ||
						imageState is ImageUiState.Loading ||
						isDecoding),
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