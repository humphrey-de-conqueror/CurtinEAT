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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.curtineat.data.remote.firebase.model.FirebaseVendorData
import com.example.curtineat.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private val vendorCategories = listOf(
	"Western",
	"Japanese",
	"Chinese",
	"Korean",
	"Local",
	"Fast Food",
	"Dessert",
	"Beverage",
	"Other"
)

private const val IMAGE_BASE_URL =
	"https://curtineat-image-api.work-gordonyewyangliew.workers.dev/images/"

@Composable
fun VendorProfileScreen(
	viewModel: AppViewModel,
	onLoginClick: () -> Unit,
	onHomeClick: () -> Unit
) {

	var vendor by remember {
		mutableStateOf<FirebaseVendorData?>(null)
	}

	var editMode by remember {
		mutableStateOf(false)
	}

	var vendorName by remember {
		mutableStateOf("")
	}

	var category by remember {
		mutableStateOf("")
	}

	var vendorImage by remember {
		mutableStateOf("")
	}

	var categoryExpanded by remember {
		mutableStateOf(false)
	}

	var loading by remember {
		mutableStateOf(true)
	}

	var saving by remember {
		mutableStateOf(false)
	}

	var uploadingImage by remember {
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

			uploadVendorImage(
				context = context,
				uri = uri,
				viewModel = viewModel
			) { imageId ->

				if (imageId != null) {
					vendorImage = imageId
				}

				uploadingImage = false
			}

			uploadingImage = true
		}

	LaunchedEffect(vendorId) {

		if (vendorId == null) {
			loading = false
			onLoginClick()
			return@LaunchedEffect
		}

		viewModel.getVendorById(
			vendorId
		) { result ->

			vendor = result

			if (result != null) {
				vendorName = result.vendorName
				category = result.category
				vendorImage = result.vendorImage
			}

			loading = false
		}
	}

	if (loading) {
		Text(
			text = "Loading..."
		)

		return
	}

	if (vendorId == null) {
		return
	}

	if (vendor == null) {
		Text(
			text = "Vendor profile not found."
		)

		return
	}

	val currentVendor =
		vendor!!

	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.verticalScroll(scrollState)
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {

			Text(
				text = "Vendor Profile"
			)

			if (!editMode) {

				Button(
					onClick = {
						editMode = true

						vendorName = currentVendor.vendorName
						category = currentVendor.category
						vendorImage = currentVendor.vendorImage
					}
				) {
					Text("Edit")
				}
			}
		}

		if (vendorImage.isBlank()) {

			Icon(
				imageVector = Icons.Default.AccountCircle,
				contentDescription = "Default vendor profile picture",
				modifier = Modifier.size(120.dp)
			)

		} else {

			AsyncImage(
				model = IMAGE_BASE_URL + vendorImage,
				contentDescription = "Vendor profile picture",
				modifier = Modifier.size(120.dp)
			)
		}

		if (editMode) {

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
						"Change Profile Picture"
					}
				)
			}
		}

		if (editMode) {

			OutlinedTextField(
				value = vendorName,
				onValueChange = {
					vendorName = it
				},
				label = {
					Text("Vendor Name")
				},
				modifier = Modifier.fillMaxWidth()
			)

		} else {

			OutlinedTextField(
				value = currentVendor.vendorName,
				onValueChange = {},
				readOnly = true,
				label = {
					Text("Vendor Name")
				},
				modifier = Modifier.fillMaxWidth()
			)
		}

		OutlinedTextField(
			value = currentVendor.vendorEmail,
			onValueChange = {},
			readOnly = true,
			label = {
				Text("Email")
			},
			modifier = Modifier.fillMaxWidth()
		)

		if (editMode) {

			Column(
				modifier = Modifier.fillMaxWidth()
			) {

				OutlinedTextField(
					value = category,
					onValueChange = {},
					readOnly = true,
					label = {
						Text("Category")
					},
					modifier = Modifier.fillMaxWidth()
				)

				DropdownMenu(
					expanded = categoryExpanded,
					onDismissRequest = {
						categoryExpanded = false
					}
				) {

					vendorCategories.forEach { item ->

						DropdownMenuItem(
							text = {
								Text(item)
							},
							onClick = {
								category = item
								categoryExpanded = false
							}
						)
					}
				}

				OutlinedButton(
					onClick = {
						categoryExpanded = true
					},
					modifier = Modifier.fillMaxWidth()
				) {
					Text(
						if (category.isEmpty()) {
							"Choose Category"
						} else {
							category
						}
					)
				}
			}

		} else {

			OutlinedTextField(
				value = currentVendor.category,
				onValueChange = {},
				readOnly = true,
				label = {
					Text("Category")
				},
				modifier = Modifier.fillMaxWidth()
			)
		}

		OutlinedTextField(
			value = currentVendor.rating.toString(),
			onValueChange = {},
			readOnly = true,
			label = {
				Text("Rating")
			},
			modifier = Modifier.fillMaxWidth()
		)

		OutlinedTextField(
			value = currentVendor.distance.toString(),
			onValueChange = {},
			readOnly = true,
			label = {
				Text("Distance")
			},
			modifier = Modifier.fillMaxWidth()
		)

		OutlinedTextField(
			value = currentVendor.moneyBalance.toString(),
			onValueChange = {},
			readOnly = true,
			label = {
				Text("Balance")
			},
			modifier = Modifier.fillMaxWidth()
		)

		if (editMode) {

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {

				OutlinedButton(
					onClick = {
						vendorName = currentVendor.vendorName
						category = currentVendor.category
						vendorImage = currentVendor.vendorImage
						editMode = false
					},
					modifier = Modifier.weight(1f)
				) {
					Text("Cancel")
				}

				Button(
					onClick = {

						saving = true

						viewModel.updateVendorProfile(
							vendorId = currentVendor.vendorId,
							vendorName = vendorName.trim(),
							category = category,
							vendorImage = vendorImage
						) { success ->

							if (success) {

								vendor =
									currentVendor.copy(
										vendorName = vendorName.trim(),
										category = category,
										vendorImage = vendorImage
									)

								editMode = false
							}

							saving = false
						}
					},
					enabled = !saving &&
						!uploadingImage &&
						vendorName.isNotBlank() &&
						category.isNotBlank(),
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

		} else {

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Button(
					onClick = {
						onHomeClick()
					},
					modifier = Modifier.weight(1f)
				) {
					Text("Back")
				}

				Button(
					onClick = {
						viewModel.logout()
						onHomeClick()
					},
					modifier = Modifier.weight(1f)
				) {
					Text("Logout")
				}
			}
		}
	}
}

private fun uploadVendorImage(
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
				"vendor_image",
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