
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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

@Composable
fun VendorProfileScreen(
	viewModel: AppViewModel,
	onLoginClick: () -> Unit,
	onHomeClick: () -> Unit
) {
	val authenticationState by
	viewModel.authenticationState.collectAsState()

	val vendorProfileState by
	viewModel.vendorProfileState.collectAsState()

	var authChecked by remember {
		mutableStateOf(false)
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

	var saving by remember {
		mutableStateOf(false)
	}

	var uploadingImage by remember {
		mutableStateOf(false)
	}

	val vendorId = authenticationState.userId
	val userRole = authenticationState.role

	LaunchedEffect(Unit) {
		viewModel.checkCurrentUserRole {
			authChecked = true
		}
	}

	LaunchedEffect(authChecked, vendorId, userRole) {
		if (authChecked) {
			if (
				vendorId == null ||
				userRole != UserRole.VENDOR
			) {
				onLoginClick()
			} else {
				viewModel.observeVendorProfile(vendorId)
			}
		}
	}

	val vendor = vendorProfileState.data

	LaunchedEffect(vendor) {
		if (vendor != null && !editMode) {
			vendorName = vendor.vendorName
			category = vendor.category
			vendorImage = vendor.vendorImage
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
						vendorImage = imageId
					}

					uploadingImage = false
				}
			}
		}

	if (!authChecked || vendorProfileState.isInitialLoading) {
		Text(text = "Loading...")
		return
	}

	if (vendorId == null || userRole != UserRole.VENDOR) {
		return
	}

	if (vendor == null) {
		Text(text = "Vendor profile not found.")
		return
	}

	val currentVendor = vendor
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
			Text(text = "Vendor Profile")

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
			CachedImage(
				imageId = vendorImage,
				viewModel = viewModel,
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