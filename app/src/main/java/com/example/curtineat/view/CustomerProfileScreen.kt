
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole

@Composable
fun CustomerProfileScreen(
	viewModel: AppViewModel,
	onLoginClick: () -> Unit,
	onHomeClick: () -> Unit
) {
	val authenticationState by
	viewModel.authenticationState.collectAsState()

	val customerState by
	viewModel.customerState.collectAsState()

	var authChecked by remember {
		mutableStateOf(false)
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

	var saving by remember {
		mutableStateOf(false)
	}

	var uploadingImage by remember {
		mutableStateOf(false)
	}

	val customerId = authenticationState.userId
	val userRole = authenticationState.role

	LaunchedEffect(Unit) {
		viewModel.checkCurrentUserRole {
			authChecked = true
		}
	}

	LaunchedEffect(authChecked, customerId, userRole) {
		if (authChecked) {
			if (
				customerId == null ||
				userRole != UserRole.CUSTOMER
			) {
				onLoginClick()
			} else {
				viewModel.observeCustomer(customerId)
			}
		}
	}

	val customer = customerState.data

	LaunchedEffect(customer) {
		if (customer != null && !editMode) {
			customerName = customer.customerName
			customerImage = customer.customerImage
		}
	}

	val imagePicker =
		rememberLauncherForActivityResult(
			contract = ActivityResultContracts.GetContent()
		) { uri: Uri? ->
			if (uri != null) {
				uploadingImage = true

				viewModel.uploadImage(uri) { imageId ->
					if (imageId != null) {
						customerImage = imageId
					}

					uploadingImage = false
				}
			}
		}

	if (!authChecked || customerState.isInitialLoading) {
		Text(text = "Loading...")
		return
	}

	if (customerId == null || userRole != UserRole.CUSTOMER) {
		return
	}

	if (customer == null) {
		Text(text = "Customer profile not found.")
		return
	}

	val currentCustomer = customer
	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.verticalScroll(scrollState)
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
			Text(text = "Customer Profile")

			if (!editMode) {
				Button(
					onClick = {
						editMode = true
						customerName = currentCustomer.customerName
						customerImage = currentCustomer.customerImage
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
				imageVector = Icons.Default.AccountCircle,
				contentDescription = "Default customer profile picture",
				modifier = Modifier.size(120.dp)
			)
		} else {
			CachedImage(
				imageId = customerImage,
				viewModel = viewModel,
				contentDescription = "Customer profile picture",
				modifier = Modifier.size(120.dp)
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
				value = currentCustomer.customerName,
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
		 */
		OutlinedTextField(
			value = currentCustomer.customerEmail,
			onValueChange = {},
			readOnly = true,
			label = {
				Text("Email")
			},
			modifier = Modifier.fillMaxWidth()
		)

		/*
		 * Balance is read-only.
		 */
		OutlinedTextField(
			value = currentCustomer.moneyBalance.toString(),
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
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				OutlinedButton(
					onClick = {
						customerName = currentCustomer.customerName
						customerImage = currentCustomer.customerImage
						editMode = false
					},
					modifier = Modifier.weight(1f)
				) {
					Text("Cancel")
				}

				Button(
					onClick = {
						saving = true

						viewModel.updateCustomerProfile(
							customerId = currentCustomer.customerId,
							customerName = customerName.trim(),
							customerImage = customerImage
						) { success ->
							if (success) {
								editMode = false
							}

							saving = false
						}
					},
					enabled = !saving &&
						!uploadingImage &&
						customerName.isNotBlank(),
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
			/*
			 * Back / Logout
			 */
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
						viewModel.signOut()
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