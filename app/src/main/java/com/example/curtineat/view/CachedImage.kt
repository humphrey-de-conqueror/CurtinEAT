//
//package com.example.curtineat.view
//
//import android.graphics.BitmapFactory
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material3.Icon
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.layout.ContentScale
//import com.example.curtineat.viewmodel.AppViewModel
//import com.example.curtineat.viewmodel.state.ImageUiState
//
//@Composable
//fun CachedImage(
//	imageId: String,
//	viewModel: AppViewModel,
//	contentDescription: String?,
//	modifier: Modifier = Modifier,
//	retryOnTap: Boolean = false
//) {
//	val imageStates by viewModel.imageStates.collectAsState()
//
//	val imageState = imageStates[imageId]
//
//	LaunchedEffect(imageId) {
//		if (imageId.isNotBlank()) {
//			viewModel.loadImage(imageId)
//		}
//	}
//
//	val bitmap = remember(imageState) {
//		val bytes = (imageState as? ImageUiState.Loaded)?.bytes
//
//		if (bytes == null) {
//			null
//		} else {
//			BitmapFactory.decodeByteArray(
//				bytes,
//				0,
//				bytes.size
//			)?.asImageBitmap()
//		}
//	}
//
//	val canRetry =
//		imageState is ImageUiState.Unavailable ||
//			(imageState is ImageUiState.Loaded && bitmap == null)
//
//	Box(
//		modifier = if (canRetry) {
//			modifier.clickable { viewModel.retryImage(imageId) }
//		} else {
//			modifier
//		}
//	) {
//		if (bitmap != null) {
//			Image(
//				bitmap = bitmap,
//				contentDescription = contentDescription,
//				modifier = Modifier.fillMaxSize(),
//				contentScale = ContentScale.Crop
//			)
//		} else {
//			Icon(
//				imageVector = Icons.Default.AccountCircle,
//				contentDescription = contentDescription,
//				modifier = Modifier.fillMaxSize()
//			)
//		}
//	}
//}
//----------------------------------------------------------
//BEFORE
//full photo decode
//→ UI thread
//→ decode again when card recreated
//→ all CachedImages observe whole map
//
//AFTER
//smaller bitmap decode
//→ background thread
//→ decoded bitmap cached in RAM
//→ each CachedImage only reacts to its own image
//----------------------------------------------------------

package com.example.curtineat.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.state.ImageUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val bitmapMemoryCache =
	object : LruCache<String, Bitmap>(24 * 1024) {

		override fun sizeOf(
			key: String,
			value: Bitmap
		): Int {
			return (value.byteCount / 1024)
				.coerceAtLeast(1)
		}
	}

@Composable
fun CachedImage(
	imageId: String,
	viewModel: AppViewModel,
	contentDescription: String?,
	modifier: Modifier = Modifier,
	retryOnTap: Boolean = false
) {
	// Only react when THIS image changes.
	val imageStateFlow = remember(
		imageId,
		viewModel
	) {
		viewModel.imageStates
			.map { states ->
				states[imageId]
			}
			.distinctUntilChanged()
	}

	val imageState by imageStateFlow.collectAsState(
		initial = null
	)

	LaunchedEffect(imageId) {
		if (imageId.isNotBlank()) {
			viewModel.loadImage(imageId)
		}
	}

	// Decode away from the UI thread and keep
	// the decoded bitmap in memory.
	val bitmap by produceState<Bitmap?>(
		initialValue = bitmapMemoryCache.get(imageId),
		key1 = imageId,
		key2 = imageState
	) {
		bitmapMemoryCache.get(imageId)?.let {
			value = it
			return@produceState
		}

		val bytes =
			(imageState as? ImageUiState.Loaded)
				?.bytes

		if (bytes == null) {
			value = null
			return@produceState
		}

		value = withContext(Dispatchers.Default) {
			decodeSampledBitmap(bytes)?.also {
				bitmapMemoryCache.put(
					imageId,
					it
				)
			}
		}
	}

	val canRetry =
		imageState is ImageUiState.Unavailable ||
				(
						imageState is ImageUiState.Loaded &&
								bitmap == null
						)

	Box(
		modifier =
			if (canRetry) {
				modifier.clickable {
					viewModel.retryImage(imageId)
				}
			} else {
				modifier
			}
	) {
		bitmap?.let { loadedBitmap ->

			Image(
				bitmap = loadedBitmap.asImageBitmap(),
				contentDescription = contentDescription,
				modifier = Modifier.fillMaxSize(),
				contentScale = ContentScale.Crop
			)

		} ?: Icon(
			// Keep/change this to your food placeholder
			// that you currently want.
			imageVector = Icons.Default.AccountCircle,
			contentDescription = contentDescription,
			modifier = Modifier.fillMaxSize()
		)
	}
}

private fun decodeSampledBitmap(
	bytes: ByteArray
): Bitmap? {

	if (bytes.isEmpty()) {
		return null
	}

	val bounds =
		BitmapFactory.Options().apply {
			inJustDecodeBounds = true
		}

	BitmapFactory.decodeByteArray(
		bytes,
		0,
		bytes.size,
		bounds
	)

	if (
		bounds.outWidth <= 0 ||
		bounds.outHeight <= 0
	) {
		return null
	}

	// Your cards don't need a 3000–5000 px image.
	val targetSize = 768

	var sampleSize = 1

	while (
		bounds.outWidth / (sampleSize * 2) >= targetSize &&
		bounds.outHeight / (sampleSize * 2) >= targetSize
	) {
		sampleSize *= 2
	}

	val options =
		BitmapFactory.Options().apply {
			inSampleSize = sampleSize
		}

	return BitmapFactory.decodeByteArray(
		bytes,
		0,
		bytes.size,
		options
	)
}