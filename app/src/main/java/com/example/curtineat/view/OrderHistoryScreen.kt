package com.example.curtineat.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.VendorEntity
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.curtineat.ui.theme.AppThemeMode

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
	onThemeModeChange: (AppThemeMode) -> Unit
) {
	val orderState by appViewModel.orderState.collectAsState()
	val vendorState by appViewModel.vendorState.collectAsState()
	val authenticationState by appViewModel.authenticationState.collectAsState()

	var role by remember { mutableStateOf<UserRole?>(null) }

	val isLoggedIn = role == UserRole.CUSTOMER || role == UserRole.VENDOR
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
		onThemeModeChange = onThemeModeChange
	) { innerPadding ->

		Column(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
		) {

			OrderHistoryTitleCard()

			LazyColumn(
				modifier = Modifier.weight(1f),
				contentPadding = PaddingValues(
					start = 16.dp,
					end = 16.dp,
					bottom = 16.dp
				),
				verticalArrangement = Arrangement.spacedBy(12.dp)
			) {

				if (role == null) {
					item {
						LoadingDots()
					}
				} else if (!isLoggedIn) {
					item {
						TextNormal(
							text = "Please log in to see your orders",
							modifier = Modifier
								.fillMaxWidth()
								.padding(32.dp),
							textAlign = TextAlign.Center,
							fontSize = 16.sp
						)
					}
				} else if (
					orderState.isInitialLoading &&
					sortedOrders.isEmpty()
				) {
					item {
						LoadingDots()
					}
				} else if (sortedOrders.isEmpty()) {
					item {
						TextNormal(
							text = "No orders yet",
							modifier = Modifier
								.fillMaxWidth()
								.padding(32.dp),
							textAlign = TextAlign.Center,
							fontSize = 16.sp
						)
					}
				} else {
					items(
						items = sortedOrders,
						key = { it.orderId }
					) { order ->

						val counterpartLabel =
							if (isVendorAccount) {
								// The current ViewModel exposes one customer
								// profile, not a list of all customers.
								"Customer"
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

@Composable
fun OrderHistoryTitleCard() {
	PrimaryCard(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			TextNormal(
				text = "Order History",
				fontWeight = FontWeight.Bold,
				fontSize = 24.sp
			)

			IconButton(
				onClick = { }
			) {
				Icon(
					imageVector = Icons.Default.FilterList,
					contentDescription = "Sort (coming soon)"
				)
			}
		}
	}
}

// Tap for receipt
@Composable
fun OrderHistoryCard(
	order: OrderEntity,
	counterpartLabel: String,
	onClick: () -> Unit
) {
	PrimaryCard(
		modifier = Modifier
			.fillMaxWidth()
			.clickable { onClick() }
	) {

		// WHO + STATUS
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			TextNormal(
				text = counterpartLabel,
				modifier = Modifier.weight(1f),
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp
			)

			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = order.status,
					color = orderStatusColor(order.status),
					fontWeight = FontWeight.Bold,
					fontSize = 14.sp
				)

				Icon(
					imageVector = Icons.Default.ChevronRight,
					contentDescription = "View receipt"
				)
			}
		}

		// DATE
		TextNormal(
			text = formatOrderDateTime(Date(order.timestamp)),
			fontSize = 12.sp
		)

		HorizontalDivider(
			modifier = Modifier.padding(vertical = 8.dp)
		)

		// PRODUCTS are stored separately in Room.
		// Their details are displayed on the receipt screen.

		HorizontalDivider(
			modifier = Modifier.padding(vertical = 8.dp)
		)

		// TOTAL
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			TextNormal(
				text = "Total",
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)

			TextNormal(
				text = "RM %.2f".format(order.totalPrice),
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)
		}
	}
}

@Composable
fun orderStatusColor(status: String): Color {
	return when (status.trim().uppercase()) {
		"PENDING" -> Color(0xFFEA7422)
		"PREPARING" -> Color(0xFF1565C0)
		"READY" -> Color(0xFF00897B)
		"COMPLETED" -> Color(0xFF2E7D32)
		"CANCELLED" -> MaterialTheme.colorScheme.error
		else -> MaterialTheme.colorScheme.onSurfaceVariant
	}
}

fun formatOrderDateTime(date: Date): String {
	return SimpleDateFormat(
		"d MMM yyyy, h:mm a",
		Locale.getDefault()
	).format(date)
}