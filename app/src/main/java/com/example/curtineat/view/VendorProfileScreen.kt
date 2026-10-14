package com.example.curtineat.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.curtineat.ui.components.AppCard
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import com.example.curtineat.ui.components.VendorCategoryDropdown

@Composable
fun VendorProfileScreen(
	viewModel: AppViewModel,
	onLoginClick: () -> Unit,
	onHomeClick: () -> Unit
) {
	val authenticationState by viewModel.authenticationState.collectAsState()
	val vendorProfileState by viewModel.vendorProfileState.collectAsState()

	var authChecked by remember { mutableStateOf(false) }
	var isLoggingOut by remember { mutableStateOf(false) }
	var editMode by remember { mutableStateOf(false) }

	var vendorName by remember { mutableStateOf("") }
	var category by remember { mutableStateOf("") }
	var vendorImage by remember { mutableStateOf("") }

	var saving by remember { mutableStateOf(false) }
	var uploadingImage by remember { mutableStateOf(false) }

	val vendorId = authenticationState.userId
	val userRole = authenticationState.role

	LaunchedEffect(Unit) {
		viewModel.checkCurrentUserRole {
			authChecked = true
		}
	}

	LaunchedEffect(authChecked, vendorId, userRole, isLoggingOut
	) {
		if (authChecked) {
			if (vendorId == null || userRole != UserRole.VENDOR
			) {
				if (!isLoggingOut) {
					onLoginClick()
				}
			} else {
				viewModel.observeVendorProfile(vendorId)
			}
		}
	}

	val vendor = vendorProfileState.data

	LaunchedEffect(vendor, editMode) {
		if (vendor != null && !editMode) {
			vendorName = vendor.vendorName
			category = vendor.category
			vendorImage = vendor.vendorImage
		}
	}

	val imagePicker = rememberLauncherForActivityResult(
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
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.windowInsetsPadding(WindowInsets.safeDrawing),
			contentAlignment = Alignment.Center
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(12.dp)
			) {
				CircularProgressIndicator()

				AppText(
					text = "Loading your profile...",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}

		return
	}

	if (vendorId == null || userRole != UserRole.VENDOR) {
		return
	}

	if (vendor == null) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.windowInsetsPadding(WindowInsets.safeDrawing)
				.padding(24.dp),
			contentAlignment = Alignment.Center
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(12.dp)
			) {
				AppText(
					text = "Profile unavailable",
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.SemiBold
				)

				AppText(
					text = "We couldn't find your vendor profile.",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				OutlinedButton(onClick = onHomeClick) {
					Text("Back to Home")
				}
			}
		}

		return
	}

	val currentVendor = vendor
	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.windowInsetsPadding(WindowInsets.safeDrawing)
			.verticalScroll(scrollState)
			.padding(horizontal = 20.dp, vertical = 20.dp),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {

		// Page heading
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			AppText(
				text = "My Profile",
				style = MaterialTheme.typography.headlineMedium,
				fontWeight = FontWeight.Bold
			)

			AppText(
				text = "Manage your restaurant information.",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}

		// Vendor profile header
		AppCard(
			modifier = Modifier
				.fillMaxWidth()
				.animateContentSize(),
			shape = RoundedCornerShape(24.dp),
			contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp)
		) {
			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.spacedBy(16.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Box(
					modifier = Modifier.size(112.dp),
					contentAlignment = Alignment.Center
				) {
					if (vendorImage.isBlank()) {
						Surface(
							modifier = Modifier.fillMaxSize(),
							shape = CircleShape,
							color = MaterialTheme.colorScheme.secondaryContainer
						) {
							Icon(
								imageVector = Icons.Default.AccountCircle,
								contentDescription = "Vendor profile picture",
								modifier = Modifier
									.fillMaxSize()
									.padding(8.dp),
								tint = MaterialTheme.colorScheme.onSecondaryContainer
							)
						}
					} else {
						CachedImage(
							imageId = vendorImage,
							viewModel = viewModel,
							contentDescription = "Vendor profile picture",
							modifier = Modifier
								.fillMaxSize()
								.clip(CircleShape),
							retryOnTap = true
						)
					}
				}

				AnimatedVisibility(
					visible = editMode,
					enter = fadeIn() + slideInVertically { -it / 3 },
					exit = fadeOut() + slideOutVertically { -it / 3 }
				) {
					OutlinedButton(
						onClick = { imagePicker.launch("image/*") },
						enabled = !uploadingImage,
						modifier = Modifier.fillMaxWidth()
					) {
						if (uploadingImage) {
							CircularProgressIndicator(
								modifier = Modifier.size(18.dp),
								strokeWidth = 2.dp
							)

							Spacer(Modifier.width(8.dp))

							Text("Uploading image...")
						} else {
							Text("Change Profile Picture")
						}
					}
				}

				if (!editMode) {
					AppText(
						text = currentVendor.vendorName,
						style = MaterialTheme.typography.headlineSmall,
						fontWeight = FontWeight.Bold,
						maxLines = 2,
						overflow = TextOverflow.Ellipsis
					)

					Surface(
						shape = RoundedCornerShape(50),
						color = MaterialTheme.colorScheme.secondaryContainer
					) {
						AppText(
							text = currentVendor.category.ifBlank {
								"Uncategorized"
							},
							modifier = Modifier.padding(
								horizontal = 14.dp,
								vertical = 7.dp
							),
							style = MaterialTheme.typography.labelLarge,
							color = MaterialTheme.colorScheme.onSecondaryContainer
						)
					}

					AppText(
						text = currentVendor.vendorEmail,
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)

					Button(
						onClick = {
							vendorName = currentVendor.vendorName
							category = currentVendor.category
							vendorImage = currentVendor.vendorImage
							editMode = true
						},
						modifier = Modifier.fillMaxWidth()
					) {
						Text("Edit Profile")
					}
				} else {
					AppText(
						text = "Edit your details",
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.SemiBold
					)

					OutlinedTextField(
						value = vendorName,
						onValueChange = { vendorName = it },
						label = { Text("Vendor Name") },
						singleLine = true,
						modifier = Modifier.fillMaxWidth()
					)

					OutlinedTextField(
						value = currentVendor.vendorEmail,
						onValueChange = {},
						readOnly = true,
						label = { Text("Email") },
						singleLine = true,
						modifier = Modifier.fillMaxWidth()
					)

					// Category selector
					Column(
						modifier = Modifier.fillMaxWidth(),
						verticalArrangement = Arrangement.spacedBy(4.dp)
					) {
						AppText(
							text = "Category",
							style = MaterialTheme.typography.labelMedium,
							color = MaterialTheme.colorScheme.onSurfaceVariant
						)

						VendorCategoryDropdown(
							selectedCategory = category,
							onCategorySelected = {
								category = it
							},
							enabled = !saving
						)
					}

					HorizontalDivider(
						color = MaterialTheme.colorScheme.outlineVariant
					)

					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(10.dp)
					) {
						OutlinedButton(
							onClick = {
								vendorName = currentVendor.vendorName
								category = currentVendor.category
								vendorImage = currentVendor.vendorImage
								editMode = false
							},
							enabled = !saving,
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
							if (saving) {
								CircularProgressIndicator(
									modifier = Modifier.size(18.dp),
									strokeWidth = 2.dp,
									color = MaterialTheme.colorScheme.onPrimary
								)

								Spacer(Modifier.width(8.dp))

								Text("Saving")
							} else {
								Text("Save Changes")
							}
						}
					}
				}
			}
		}

		// Account information
		AppCard(
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(20.dp),
			contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp)
		) {
			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.spacedBy(14.dp)
			) {
				AppText(
					text = "Account Information",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.SemiBold
				)

				ProfileInfoRow(
					label = "Vendor ID",
					value = currentVendor.vendorId
				)

				HorizontalDivider(
					color = MaterialTheme.colorScheme.outlineVariant
				)

				ProfileInfoRow(
					label = "Email Address",
					value = currentVendor.vendorEmail
				)

				HorizontalDivider(
					color = MaterialTheme.colorScheme.outlineVariant
				)

				ProfileInfoRow(
					label = "Category",
					value = currentVendor.category
				)
			}
		}

		// Navigation and sign-out actions
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 4.dp, bottom = 12.dp),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			OutlinedButton(
				onClick = onHomeClick,
				modifier = Modifier.weight(1f)
			) {
				Text("Back to Home")
			}

			Button(
				onClick = {
					isLoggingOut = true
					viewModel.signOut()
					onHomeClick()
				},
				modifier = Modifier.weight(1f),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.error,
					contentColor = MaterialTheme.colorScheme.onError
				)
			) {
				Text("Logout")
			}
		}
	}
}

@Composable
private fun VendorStatCard(
	title: String,
	value: String,
	modifier: Modifier = Modifier
) {
	AppCard(
		modifier = modifier
			.height(106.dp)
			.animateContentSize(),
		shape = RoundedCornerShape(18.dp),
		containerColor = MaterialTheme.colorScheme.secondaryContainer,
		contentPadding = androidx.compose.foundation.layout.PaddingValues(
			horizontal = 8.dp,
			vertical = 14.dp
		)
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			AppText(
				text = title,
				style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			Spacer(Modifier.height(8.dp))

			AppText(
				text = value,
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}

@Composable
private fun ProfileInfoRow(
	label: String,
	value: String
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		AppText(
			text = label,
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)

		AppText(
			text = value.ifBlank { "Not provided" },
			style = MaterialTheme.typography.bodyLarge,
			fontWeight = FontWeight.Medium
		)
	}
}