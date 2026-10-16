package com.example.curtineat.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.ui.components.AppCard
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.ui.theme.AppThemeMode
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ---------------- ORDER HISTORY SCREEN ----------------

@Composable
fun OrderHistoryScreen(
	appViewModel: AppViewModel,
	onHomeClick: () -> Unit,
	onProfileClick: () -> Unit,
	onWalletClick: () -> Unit,
	onLoginClick: () -> Unit,
	onHistoryClick: () -> Unit,
	onOrderClick: (String) -> Unit,
	themeMode: AppThemeMode,
	onThemeModeChange: (AppThemeMode) -> Unit,
	onLogoutClick: () -> Unit,
) {
	val orderState by appViewModel.orderState.collectAsState()
	val vendorState by appViewModel.vendorState.collectAsState()
	val authenticationState by appViewModel.authenticationState.collectAsState()
	val customerNames by appViewModel.customerNames.collectAsState()

	var role by remember {
		mutableStateOf<UserRole?>(null)
	}

	val isLoggedIn = role == UserRole.CUSTOMER ||
		role == UserRole.VENDOR

	val isVendorAccount = role == UserRole.VENDOR

	LaunchedEffect(Unit) {
		appViewModel.checkCurrentUserRole {
			role = it
		}
	}

	LaunchedEffect(role, authenticationState.userId) {
		val userId = authenticationState.userId

		if (userId != null) {
			when (role) {
				UserRole.CUSTOMER ->
					appViewModel.observeOrdersByCustomer(userId)

				UserRole.VENDOR ->
					appViewModel.observeOrdersByVendor(userId)

				else -> Unit
			}
		}
	}

	val sortedOrders = orderState.data.sortedByDescending {
		it.timestamp
	}

	val customerIds = sortedOrders.map { it.customerId }.toSet()

	LaunchedEffect(isVendorAccount, customerIds) {
		if (isVendorAccount) {
			appViewModel.loadCustomerNames(customerIds)
		}
	}

	AppScaffold(
		appViewModel = appViewModel,
		onHomeClick = onHomeClick,
		onProfileClick = onProfileClick,
		onWalletClick = onWalletClick,
		onLoginClick = onLoginClick,
		onHistoryClick = onHistoryClick,
		showSearch = false,
		showNotifications = true,
		themeMode = themeMode,
		onThemeModeChange = onThemeModeChange,
		onLogoutClick = onLogoutClick,
	) { innerPadding ->

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			OrderHistoryTitleCard(
				orderCount = sortedOrders.size
			)

			LazyColumn(
				modifier = Modifier
					.fillMaxSize(),
				contentPadding = PaddingValues(
					start = 16.dp,
					end = 16.dp,
					top = 4.dp,
					bottom = 24.dp
				),
				verticalArrangement = Arrangement.spacedBy(12.dp)
			) {
				when {
					role == null -> {
						item {
							LoadingDots()
						}
					}

					!isLoggedIn -> {
						item {
							OrderHistoryEmptyState(
								title = "Log in to view your orders",
								description = "Your past orders and receipts will appear here after you sign in."
							)
						}
					}

					orderState.isInitialLoading &&
						sortedOrders.isEmpty() -> {
						item {
							LoadingDots()
						}
					}

					sortedOrders.isEmpty() -> {
						item {
							OrderHistoryEmptyState(
								title = "No orders yet",
								description = "Once you place an order, its status and receipt will appear here."
							)
						}
					}

					else -> {
						items(
							items = sortedOrders,
							key = { it.orderId }
						) { order ->
							// the name parts
							val counterpartLabel = if (isVendorAccount) {
								customerNames[order.customerId] ?: "Customer"
							} else {
								vendorState.data.firstOrNull {
									it.vendorId == order.vendorId
								}?.vendorName ?: "Vendor"
							}

							OrderHistoryCard(
								order = order,
								counterpartLabel = counterpartLabel,
								onClick = {
									onOrderClick(order.orderId)
								}
							)
						}
					}
				}
			}
		}
	}
}

// ---------------- TITLE ----------------

@Composable
fun OrderHistoryTitleCard(
	orderCount: Int = 0
) {
	AppCard(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		contentPadding = PaddingValues(20.dp)
	) {
		AppText(
			text = "Order History",
			style = MaterialTheme.typography.headlineSmall,
			fontWeight = FontWeight.Bold
		)

		Spacer(Modifier.size(4.dp))

		AppText(
			text = if (orderCount == 1) {
				"1 order"
			} else {
				"$orderCount orders"
			},
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
	}
}

// ---------------- ORDER CARD ----------------

@Composable
fun OrderHistoryCard(
	order: OrderEntity,
	counterpartLabel: String,
	onClick: () -> Unit
) {
	AppCard(
		modifier = Modifier.fillMaxWidth(),
		onClick = onClick,
		contentPadding = PaddingValues(16.dp)
	) {
		// Counterpart and status
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(
				modifier = Modifier.weight(1f),
				verticalArrangement = Arrangement.spacedBy(4.dp)
			) {
				AppText(
					text = counterpartLabel,
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.SemiBold,
					maxLines = 2
				)

				AppText(
					text = "Order #${order.orderId.takeLast(8)}",
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}

			Spacer(Modifier.size(8.dp))

			OrderStatusBadge(
				status = order.status
			)
		}

		Spacer(Modifier.size(12.dp))

		// Order date
		AppText(
			text = formatOrderDateTime(Date(order.timestamp)),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)

		Spacer(Modifier.size(16.dp))

		// Total and receipt navigation
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(2.dp)
			) {
				AppText(
					text = "Total amount",
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				AppText(
					text = "RM %.2f".format(order.totalPrice),
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.primary
				)
			}

			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				AppText(
					text = "View receipt",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.primary
				)

				Spacer(Modifier.size(4.dp))

				androidx.compose.material3.Icon(
					imageVector = Icons.Default.ChevronRight,
					contentDescription = "View receipt",
					tint = MaterialTheme.colorScheme.primary
				)
			}
		}
	}
}

// ---------------- STATUS BADGE ----------------

@Composable
fun OrderStatusBadge(
	status: String
) {
	val normalizedStatus = status.trim().uppercase()

	val statusColor = orderStatusColor(normalizedStatus)

	Surface(
		shape = RoundedCornerShape(50),
		color = statusColor.copy(alpha = 0.12f)
	) {
		AppText(
			text = normalizedStatus.ifBlank { "UNKNOWN" },
			modifier = Modifier.padding(
				horizontal = 10.dp,
				vertical = 6.dp
			),
			style = MaterialTheme.typography.labelMedium,
			color = statusColor,
			fontWeight = FontWeight.SemiBold
		)
	}
}

// ---------------- EMPTY STATE ----------------

@Composable
fun OrderHistoryEmptyState(
	title: String,
	description: String
) {
	AppCard(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 8.dp),
		contentPadding = PaddingValues(
			horizontal = 24.dp,
			vertical = 36.dp
		)
	) {
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(10.dp)
		) {
			AppText(
				text = title,
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.SemiBold,
				textAlign = TextAlign.Center
			)

			AppText(
				text = description,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				textAlign = TextAlign.Center
			)
		}
	}
}

// ---------------- STATUS COLORS ----------------

@Composable
fun orderStatusColor(status: String): Color {
	return when (status.trim().uppercase()) {
		"PENDING" ->
			MaterialTheme.colorScheme.tertiary

		"PREPARING" ->
			MaterialTheme.colorScheme.primary

		"READY" ->
			MaterialTheme.colorScheme.secondary

		"COMPLETED" ->
			Color(0xFF2E7D32)
		// hard coded, find replacement

		"CANCELLED" ->
			MaterialTheme.colorScheme.error

		else ->
			MaterialTheme.colorScheme.onSurfaceVariant
	}
}

// ---------------- DATE FORMATTING ----------------

fun formatOrderDateTime(date: Date): String {
	return SimpleDateFormat(
		"d MMM yyyy, h:mm a",
		Locale.getDefault()
	).format(date)
}