
package com.example.curtineat.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import androidx.compose.material3.ButtonDefaults
import com.example.curtineat.ui.components.AppCard

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

	var isLoggingOut by remember {
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

	var errorMessage by remember {
		mutableStateOf<String?>(null)
	}

	val customerId = authenticationState.userId
	val userRole = authenticationState.role

	LaunchedEffect(Unit) {
		viewModel.checkCurrentUserRole {
			authChecked = true
		}
	}

	LaunchedEffect(authChecked, customerId, userRole, isLoggingOut) {
		if (authChecked) {
			if (customerId == null || userRole != UserRole.CUSTOMER
			) {
				if (!isLoggingOut) {
					onLoginClick()
				}
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

	val imagePicker = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { uri: Uri? ->
		if (uri != null) {
			uploadingImage = true
			errorMessage = null

			viewModel.uploadImage(uri) { imageId ->
				if (imageId != null) {
					customerImage = imageId
				} else {
					errorMessage = "Unable to upload profile picture."
				}

				uploadingImage = false
			}
		}
	}

	if (!authChecked || customerState.isInitialLoading) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.windowInsetsPadding(WindowInsets.safeDrawing),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator()
		}
		return
	}

	if (
		customerId == null ||
		userRole != UserRole.CUSTOMER
	) {
		return
	}

	val currentCustomer = customer

	if (currentCustomer == null) {
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
				Icon(
					imageVector = Icons.Default.AccountCircle,
					contentDescription = null,
					modifier = Modifier.size(64.dp),
					tint = MaterialTheme.colorScheme.onSurfaceVariant
				)

				Text(
					text = "Customer profile not found.",
					style = MaterialTheme.typography.titleMedium
				)

				OutlinedButton(onClick = onHomeClick) {
					Text("Back to Home")
				}
			}
		}
		return
	}

	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.windowInsetsPadding(WindowInsets.safeDrawing)
			.verticalScroll(scrollState)
			.padding(
				horizontal = 20.dp,
				vertical = 24.dp
			),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		// Page heading
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
//			Column(
//				modifier = Modifier.weight(1f),
//				verticalArrangement = Arrangement.spacedBy(4.dp)
//			) {
				Text(
					text = if (editMode) {
						"Edit Profile"
					} else {
						"My Profile"
					},
					style = MaterialTheme.typography.headlineMedium,
					fontWeight = FontWeight.Bold
				)

//				Text(
//					text = if (editMode) {
//						"Update your personal information."
//					} else {
//						"Manage your account and personal details."
//					},
//					style = MaterialTheme.typography.bodyMedium,
//					color = MaterialTheme.colorScheme.onSurfaceVariant
//				)
//			}

			if (!editMode) {
				OutlinedButton(
					onClick = {
						customerName = currentCustomer.customerName
						customerImage = currentCustomer.customerImage
						errorMessage = null
						editMode = true
					}
				) {
					Icon(
						imageVector = Icons.Default.Edit,
						contentDescription = null,
						modifier = Modifier.size(18.dp)
					)
					Spacer(Modifier.width(6.dp))
					Text("Edit")
				}
			}
		}

		// Profile summary
		AppCard(
			modifier = Modifier
				.fillMaxWidth()
				.animateContentSize(),
			shape = RoundedCornerShape(24.dp),
			contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(12.dp)
			) {
				Box(
					modifier = Modifier.size(124.dp),
					contentAlignment = Alignment.Center
				) {
					Surface(
						modifier = Modifier.fillMaxSize(),
						shape = CircleShape,
						color = MaterialTheme.colorScheme.surface
					) {
						Box(contentAlignment = Alignment.Center) {
							if (customerImage.isBlank()) {
								Icon(
									imageVector = Icons.Default.AccountCircle,
									contentDescription = "Default customer profile picture",
									modifier = Modifier.size(116.dp),
									tint = MaterialTheme.colorScheme.onSurfaceVariant
								)
							} else {
								CachedImage(
									imageId = customerImage,
									viewModel = viewModel,
									contentDescription = "Customer profile picture",
									modifier = Modifier
										.fillMaxSize()
										.clip(CircleShape),
									retryOnTap = true
								)
							}

							if (uploadingImage) {
								Surface(
									modifier = Modifier.fillMaxSize(),
									shape = CircleShape,
									color = MaterialTheme.colorScheme.scrim.copy(
										alpha = 0.6f
									)
								) {
									Box(contentAlignment = Alignment.Center) {
										CircularProgressIndicator(
											modifier = Modifier.size(32.dp),
											color = MaterialTheme.colorScheme.inversePrimary
										)
									}
								}
							}
						}
					}
				}

				Text(
					text = currentCustomer.customerName,
					style = MaterialTheme.typography.headlineSmall,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onPrimaryContainer
				)

				Text(
					text = currentCustomer.customerEmail,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onPrimaryContainer
				)

				if (editMode) {
					OutlinedButton(
						onClick = {
							imagePicker.launch("image/*")
						},
						enabled = !uploadingImage && !saving
					) {
						Icon(
							imageVector = Icons.Default.Image,
							contentDescription = null,
							modifier = Modifier.size(18.dp)
						)
						Spacer(Modifier.width(8.dp))
						Text(
							if (uploadingImage) {
								"Uploading..."
							} else {
								"Change Profile Picture"
							}
						)
					}
				}
			}
		}

		// Account details
		Card(
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(24.dp),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.surface
			),
			elevation = CardDefaults.cardElevation(
				defaultElevation = 1.dp
			)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(20.dp),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Text(
					text = "Account Details",
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.SemiBold
				)

				if (editMode) {
					OutlinedTextField(
						value = customerName,
						onValueChange = {
							customerName = it
							errorMessage = null
						},
						label = {
							Text("Customer Name")
						},
						singleLine = true,
						enabled = !saving,
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
						leadingIcon = {
							Icon(
								imageVector = Icons.Default.AccountCircle,
								contentDescription = null
							)
						},
						modifier = Modifier.fillMaxWidth()
					)
				}

				OutlinedTextField(
					value = currentCustomer.customerEmail,
					onValueChange = {},
					readOnly = true,
					label = {
						Text("Email")
					},
					leadingIcon = {
						Icon(
							imageVector = Icons.Default.AlternateEmail,
							contentDescription = null
						)
					},
					modifier = Modifier.fillMaxWidth()
				)

//				HorizontalDivider(
//					color = MaterialTheme.colorScheme.outlineVariant
//				)
//
//				Row(
//					modifier = Modifier.fillMaxWidth(),
//					verticalAlignment = Alignment.CenterVertically,
//					horizontalArrangement = Arrangement.spacedBy(14.dp)
//				) {
//					Surface(
//						shape = RoundedCornerShape(14.dp),
//						color = MaterialTheme.colorScheme.secondaryContainer
//					) {
//						Icon(
//							imageVector = Icons.Default.Payments,
//							contentDescription = null,
//							modifier = Modifier
//								.size(48.dp)
//								.padding(12.dp),
//							tint = MaterialTheme.colorScheme.onSecondaryContainer
//						)
//					}
//
//					Column(
//						modifier = Modifier.weight(1f),
//						verticalArrangement = Arrangement.spacedBy(4.dp)
//					) {
//						Text(
//							text = "Account Balance",
//							style = MaterialTheme.typography.titleSmall,
//							color = MaterialTheme.colorScheme.onSurfaceVariant
//						)
//
//						Text(
//							text = "RM %.2f".format(
//								currentCustomer.moneyBalance
//							),
//							style = MaterialTheme.typography.headlineSmall,
//							fontWeight = FontWeight.Bold
//						)
//					}
//				}
			}
		}

		// Error feedback
		AnimatedVisibility(
			visible = errorMessage != null,
			enter = fadeIn(),
			exit = fadeOut()
		) {
			Surface(
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(14.dp),
				color = MaterialTheme.colorScheme.errorContainer
			) {
				Text(
					text = errorMessage.orEmpty(),
					modifier = Modifier.padding(14.dp),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onErrorContainer
				)
			}
		}

		// Actions
		if (editMode) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(12.dp)
			) {
				OutlinedButton(
					onClick = {
						customerName = currentCustomer.customerName
						customerImage = currentCustomer.customerImage
						errorMessage = null
						editMode = false
					},
					enabled = !saving && !uploadingImage,
					modifier = Modifier.weight(1f)
				) {
					Text("Cancel")
				}

				Button(
					onClick = {
						if (customerName.isBlank()) {
							errorMessage = "Customer name cannot be empty."
							return@Button
						}

						saving = true
						errorMessage = null

						viewModel.updateCustomerProfile(
							customerId = currentCustomer.customerId,
							customerName = customerName.trim(),
							customerImage = customerImage
						) { success ->
							if (success) {
								editMode = false
							} else {
								errorMessage =
									"Unable to update your profile. Please try again."
							}

							saving = false
						}
					},
					enabled = !saving &&
						!uploadingImage &&
						customerName.isNotBlank(),
					modifier = Modifier.weight(1f)
				) {
					if (saving) {
						CircularProgressIndicator(
							modifier = Modifier.size(18.dp),
							strokeWidth = 2.dp,
							color = MaterialTheme.colorScheme.onPrimary
						)
						Spacer(Modifier.width(8.dp))
						Text("Saving...")
					} else {
						Icon(
							imageVector = Icons.Default.Save,
							contentDescription = null,
							modifier = Modifier.size(18.dp)
						)
						Spacer(Modifier.width(8.dp))
						Text("Save Changes")
					}
				}
			}
		} else {
			Button(
				onClick = onHomeClick,
				modifier = Modifier.fillMaxWidth()
			) {
				Icon(
					imageVector = Icons.Default.ArrowBack,
					contentDescription = null,
					modifier = Modifier.size(18.dp)
				)
				Spacer(Modifier.width(8.dp))
				Text("Back to Home")
			}

			Button(
				onClick = {
					isLoggingOut = true
					viewModel.signOut()
					onHomeClick()
				},
				modifier = Modifier.fillMaxWidth(),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.error,
					contentColor = MaterialTheme.colorScheme.onError
				)
			) {
				Icon(
					imageVector = Icons.Default.Logout,
					contentDescription = null,
					modifier = Modifier.size(18.dp)
				)

				Spacer(Modifier.width(8.dp))

				Text("Logout")
			}
		}

		Spacer(Modifier.height(4.dp))
	}
}