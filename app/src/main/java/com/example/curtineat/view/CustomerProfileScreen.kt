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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
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
import com.example.curtineat.data.remote.firebase.model.FirebaseCustomerData
import com.example.curtineat.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private const val IMAGE_BASE_URL =
	"https://curtineat-image-api.work-gordonyewyangliew.workers.dev/images/"

@Composable
fun CustomerProfileScreen(
	viewModel: AppViewModel,
	onLoginClick: () -> Unit,
	onHomeClick: () -> Unit
) {
	var customer by remember {
		mutableStateOf<FirebaseCustomerData?>(null)
	}

	var editMode by remember {
		mutableStateOf(false)
	}

	var customerName by remember {
		mutableStateOf("")
	}

	var customerImage by remember {
		mutableStateOf("")
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

	/*
	 * Keep this check inside the screen as well.
	 *
	 * Navigation should normally prevent an unauthenticated
	 * user from reaching this screen, but the screen itself
	 * should not trust navigation as its only protection.
	 */
	val currentUser =
		FirebaseAuth.getInstance().currentUser

	val customerId =
		currentUser?.uid

	val imagePicker =
		rememberLauncherForActivityResult(
			contract = ActivityResultContracts.GetContent()
		) { uri: Uri? ->

			if (uri == null) {
				return@rememberLauncherForActivityResult
			}

			uploadCustomerImage(
				context = context,
				uri = uri,
				viewModel = viewModel
			) { imageId ->

				if (imageId != null) {
					customerImage = imageId
				}

				uploadingImage = false
			}

			uploadingImage = true
		}

	/*
	 * Load the customer belonging to the authenticated
	 * Firebase user.
	 */
	LaunchedEffect(customerId) {

		if (customerId == null) {
			loading = false
			onLoginClick()
			return@LaunchedEffect
		}

		viewModel.getCustomerById(
			customerId
		) { result ->

			customer = result

			if (result != null) {
				customerName =
					result.customerName

				customerImage =
					result.customerImage
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

	/*
	 * Defensive check in case authentication state is
	 * unavailable after the LaunchedEffect.
	 */
	if (customerId == null) {
		return
	}

	/*
	 * Firebase Authentication succeeded, but the corresponding
	 * customer document does not exist.
	 */
	if (customer == null) {
		Text(
			text = "Customer profile not found."
		)

		return
	}

	val currentCustomer =
		customer!!

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {

		/*
		 * Header
		 */
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {

			Text(
				text = "Customer Profile"
			)

			if (!editMode) {

				Button(
					onClick = {

						editMode = true

						customerName =
							currentCustomer.customerName

						customerImage =
							currentCustomer.customerImage
					}
				) {
					Text("Edit")
				}
			}
		}

		/*
		 * Profile picture
		 */
		if (customerImage.isBlank()) {

			Icon(
				imageVector =
					Icons.Default.AccountCircle,
				contentDescription =
					"Default customer profile picture",
				modifier =
					Modifier.size(120.dp)
			)

		} else {

			AsyncImage(
				model =
					IMAGE_BASE_URL +
						customerImage,
				contentDescription =
					"Customer profile picture",
				modifier =
					Modifier.size(120.dp)
			)
		}

		/*
		 * Change profile picture
		 */
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

		/*
		 * Customer name
		 */
		if (editMode) {

			OutlinedTextField(
				value = customerName,
				onValueChange = {
					customerName = it
				},
				label = {
					Text("Customer Name")
				},
				modifier = Modifier.fillMaxWidth()
			)

		} else {

			OutlinedTextField(
				value =
					currentCustomer.customerName,
				onValueChange = {},
				readOnly = true,
				label = {
					Text("Customer Name")
				},
				modifier = Modifier.fillMaxWidth()
			)
		}

		/*
		 * Email is read-only.
		 *
		 * This comes from the customer's Firestore data.
		 * The profile update function deliberately does not
		 * allow this field to be changed.
		 */
		OutlinedTextField(
			value =
				currentCustomer.customerEmail,
			onValueChange = {},
			readOnly = true,
			label = {
				Text("Email")
			},
			modifier = Modifier.fillMaxWidth()
		)

		/*
		 * Balance is read-only.
		 *
		 * Customer profile updates cannot modify this field.
		 */
		OutlinedTextField(
			value =
				currentCustomer.moneyBalance.toString(),
			onValueChange = {},
			readOnly = true,
			label = {
				Text("Balance")
			},
			modifier = Modifier.fillMaxWidth()
		)

		/*
		 * Save / Cancel
		 */
		if (editMode) {

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement =
					Arrangement.spacedBy(8.dp)
			) {

				OutlinedButton(
					onClick = {

						customerName =
							currentCustomer.customerName

						customerImage =
							currentCustomer.customerImage

						editMode = false
					},
					modifier =
						Modifier.weight(1f)
				) {

					Text("Cancel")
				}

				Button(
					onClick = {

						saving = true

						viewModel.updateCustomerProfile(
							customerId =
								currentCustomer.customerId,

							customerName =
								customerName.trim(),

							customerImage =
								customerImage
						) { success ->

							if (success) {

								customer =
									currentCustomer.copy(
										customerName =
											customerName.trim(),

										customerImage =
											customerImage
									)

								editMode = false
							}

							saving = false
						}
					},
					enabled =
						!saving &&
							!uploadingImage &&
							customerName.isNotBlank(),
					modifier =
						Modifier.weight(1f)
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

			/*
			 * Logout
			 */
			Button(
				onClick = {

					viewModel.logout()
					onHomeClick()
				},
				modifier = Modifier.fillMaxWidth()
			) {

				Text("Logout")
			}
		}
	}
}

private fun uploadCustomerImage(
	context: Context,
	uri: Uri,
	viewModel: AppViewModel,
	onResult: (String?) -> Unit
) {

	try {

		val inputStream =
			context.contentResolver
				.openInputStream(uri)
				?: throw Exception(
					"Unable to open image"
				)

		val file =
			File.createTempFile(
				"customer_image",
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