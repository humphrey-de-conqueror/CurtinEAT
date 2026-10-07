package com.example.curtineat.view

import android.content.Context
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.curtineat.data.remote.firebase.model.FirebaseProductData
import com.example.curtineat.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

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

	val context =
		LocalContext.current

	val currentUser =
		FirebaseAuth.getInstance().currentUser

	val vendorId =
		currentUser?.uid

	val imagePicker =
		rememberLauncherForActivityResult(
			contract = ActivityResultContracts.GetContent()
		) { uri: Uri? ->

			if (uri == null) {
				return@rememberLauncherForActivityResult
			}

			uploadingImage = true

			uploadProductImage(
				context = context,
				uri = uri,
				viewModel = viewModel
			) { imageId ->

				if (imageId != null) {
					productImage = imageId
				}

				uploadingImage = false
			}
		}

	val scrollState =
		rememberScrollState()

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

			androidx.compose.material3.Icon(
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
			enabled = !uploadingImage,
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
			},
			label = {
				Text("Product Name")
			},
			modifier = Modifier.fillMaxWidth()
		)

		OutlinedTextField(
			value = productPrice,
			onValueChange = {
				productPrice = it
			},
			label = {
				Text("Price")
			},
			modifier = Modifier.fillMaxWidth()
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
				}
			)
		}

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {

			OutlinedButton(
				onClick = {
					onBackClick()
				},
				modifier = Modifier.weight(1f)
			) {
				Text("Cancel")
			}

			Button(
				onClick = {

					if (vendorId == null) {
						onBackClick()
						return@Button
					}

					val price =
						productPrice.toDoubleOrNull()

					if (price == null) {
						return@Button
					}

					saving = true

					val product =
						FirebaseProductData(
							productId = "",
							vendorId = vendorId,
							productName = productName.trim(),
							productPrice = price,
							productImage = productImage,
							isAvailable = isAvailable
						)

					viewModel.addProduct(
						product
					) { success ->

						saving = false

						if (success) {
							onBackClick()
						}
					}
				},
				enabled = !saving &&
					!uploadingImage &&
					vendorId != null &&
					productName.isNotBlank() &&
					productPrice.toDoubleOrNull() != null &&
					productPrice.toDoubleOrNull()!! >= 0.0 &&
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

private fun uploadProductImage(
	context: Context,
	uri: Uri,
	viewModel: AppViewModel,
	onResult: (String?) -> Unit
) {

	try {

		val inputStream =
			context.contentResolver.openInputStream(uri)
				?: throw Exception("Unable to open image")

		val file =
			File.createTempFile(
				"product_image",
				".jpg",
				context.cacheDir
			)

		inputStream.use { input ->
			file.outputStream().use { output ->
				input.copyTo(output)
			}
		}

		val requestBody =
			file.asRequestBody(
				"image/*".toMediaType()
			)

		val multipartBody =
			MultipartBody.Part.createFormData(
				"image",
				file.name,
				requestBody
			)

		viewModel.uploadImage(
			multipartBody
		) { imageId ->

			file.delete()

			onResult(imageId)
		}

	} catch (e: Exception) {

		onResult(null)
	}
}