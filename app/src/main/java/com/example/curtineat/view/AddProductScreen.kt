package com.example.curtineat.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole

private const val IMAGE_BASE_URL =
	"https://curtineat-image-api.work-gordonyewyangliew.workers.dev/images/"

@Composable
fun AddProductScreen(
	viewModel: AppViewModel,
	onBackClick: () -> Unit
) {
	var productName by remember {
		mutableStateOf("")
	}

	var productPrice by remember {
		mutableStateOf("")
	}

	var productImage by remember {
		mutableStateOf("")
	}

	var isAvailable by remember {
		mutableStateOf(true)
	}

	var uploadingImage by remember {
		mutableStateOf(false)
	}

	var saving by remember {
		mutableStateOf(false)
	}

	var errorMessage by remember {
		mutableStateOf<String?>(null)
	}

	val authenticationState by
	viewModel.authenticationState.collectAsState()

	val vendorId = authenticationState.userId.takeIf {
		authenticationState.role == UserRole.VENDOR
	}

	val imagePicker = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { uri: Uri? ->
		if (uri != null) {
			uploadingImage = true
			errorMessage = null

			viewModel.uploadImage(uri) { imageId ->
				if (imageId != null) {
					productImage = imageId
				} else {
					errorMessage = "Unable to upload product image."
				}

				uploadingImage = false
			}
		}
	}

	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.verticalScroll(scrollState)
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {
		Text(
			text = "Add Food"
		)

		if (productImage.isBlank()) {
			Icon(
				imageVector = Icons.Default.Fastfood,
				contentDescription = "Default product image",
				modifier = Modifier.size(120.dp)
			)
		} else {
			AsyncImage(
				model = IMAGE_BASE_URL + productImage,
				contentDescription = "Product image",
				modifier = Modifier.size(120.dp),
				contentScale = ContentScale.Crop
			)
		}

		OutlinedButton(
			onClick = {
				imagePicker.launch("image/*")
			},
			enabled = !uploadingImage && !saving,
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				if (uploadingImage) {
					"Uploading..."
				} else {
					"Upload Product Picture"
				}
			)
		}

		OutlinedTextField(
			value = productName,
			onValueChange = {
				productName = it
				errorMessage = null
			},
			label = {
				Text("Product Name")
			},
			modifier = Modifier.fillMaxWidth(),
			enabled = !saving
		)

		OutlinedTextField(
			value = productPrice,
			onValueChange = {
				productPrice = it
				errorMessage = null
			},
			label = {
				Text("Price")
			},
			modifier = Modifier.fillMaxWidth(),
			enabled = !saving
		)

		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = "Available"
			)

			Switch(
				checked = isAvailable,
				onCheckedChange = {
					isAvailable = it
				},
				enabled = !saving
			)
		}

		if (errorMessage != null) {
			Text(
				text = errorMessage!!
			)
		}

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			OutlinedButton(
				onClick = onBackClick,
				enabled = !saving && !uploadingImage,
				modifier = Modifier.weight(1f)
			) {
				Text("Cancel")
			}

			val parsedPrice = productPrice.toDoubleOrNull()
			val validPrice = parsedPrice != null &&
				parsedPrice.isFinite() &&
				parsedPrice >= 0.0

			Button(
				onClick = {
					val price = productPrice.toDoubleOrNull()

					if (vendorId == null) {
						errorMessage = "Please sign in with a vendor account."
						return@Button
					}

					if (price == null || !price.isFinite() || price < 0.0) {
						errorMessage = "Enter a valid non-negative price."
						return@Button
					}

					saving = true
					errorMessage = null

					viewModel.addProduct(
						vendorId = vendorId,
						productName = productName.trim(),
						productPrice = price,
						productImage = productImage,
						isAvailable = isAvailable
					) { success ->
						saving = false

						if (success) {
							onBackClick()
						} else {
							errorMessage = "Unable to save product. Please try again."
						}
					}
				},
				enabled = !saving &&
					!uploadingImage &&
					vendorId != null &&
					productName.isNotBlank() &&
					validPrice &&
					productImage.isNotBlank(),
				modifier = Modifier.weight(1f)
			) {
				Text(
					if (saving) {
						"Saving..."
					} else {
						"Save"
					}
				)
			}
		}
	}
}
