package com.example.curtineat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import com.example.curtineat.data.remote.firebase.model.FirebaseVendorData
import com.example.curtineat.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth

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

	var categoryExpanded by remember {
		mutableStateOf(false)
	}

	var loading by remember {
		mutableStateOf(true)
	}

	var saving by remember {
		mutableStateOf(false)
	}

	val currentUser =
		FirebaseAuth.getInstance().currentUser

	val vendorId =
		currentUser?.uid

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

	Column(
		modifier = Modifier
			.fillMaxWidth()
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
					}
				) {
					Text("Edit")
				}
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
							category = category
						) { success ->

							if (success) {

								vendor =
									currentVendor.copy(
										vendorName = vendorName.trim(),
										category = category
									)

								editMode = false
							}

							saving = false
						}
					},
					enabled = !saving &&
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
		}

		if (!editMode) {

			Button(
				onClick = {

					FirebaseAuth
						.getInstance()
						.signOut()

					viewModel.clearCart()

					onHomeClick()
				},
				modifier = Modifier.fillMaxWidth()
			) {
				Text("Logout")
			}
		}
	}
}