package com.example.curtineat.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.ui.components.AppCard
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.ui.theme.AppThemeMode
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.curtineat.view.CachedImage


@Composable
fun VendorLandingScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFoodClick :(String) -> Unit,
    onAddProductClick: () -> Unit,
    themeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit
) {
    val authenticationState by
    appViewModel.authenticationState.collectAsState()

    val vendorProfileState by
    appViewModel.vendorProfileState.collectAsState()

    val productState by
    appViewModel.productState.collectAsState()

    val orderState by
    appViewModel.orderState.collectAsState()

    val loggedInVendorId = authenticationState.userId.takeIf {
        authenticationState.role == UserRole.VENDOR
    }

    var ordersExpanded by rememberSaveable {
        mutableStateOf(true)
    }

    var productsExpanded by rememberSaveable {
        mutableStateOf(true)
    }

    val currentVendor = vendorProfileState.data

    val vendorProducts = productState.data.filter {
        it.vendorId == loggedInVendorId
    }

    val activeOrders = orderState.data
        .filter {
            it.vendorId == loggedInVendorId &&
                    it.status.trim().uppercase() != "COMPLETED"
        }
        .sortedByDescending {
            it.timestamp
        }

    LaunchedEffect(loggedInVendorId) {
        if (loggedInVendorId != null) {
            appViewModel.observeVendorProfile(loggedInVendorId)
            appViewModel.refreshProductsByVendor(loggedInVendorId)
            appViewModel.observeOrdersByVendor(loggedInVendorId)
        }
    }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        onHistoryClick = onHistoryClick,
        onProfileClick = onProfileClick,
        showSearch = false,
        showNotifications = true,
        themeMode = themeMode,
        onLogoutClick = onLogoutClick,
        onThemeModeChange = onThemeModeChange,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add product"
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = 18.dp,
                vertical = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Dashboard heading
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    AppText(
                        text = "Vendor Dashboard",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    AppText(
                        text = currentVendor?.vendorName?.let {
                            "Welcome back, $it"
                        } ?: "Manage your orders and products.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Dashboard summary
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    VendorSummaryCard(
                        title = "Active Orders",
                        value = activeOrders.size.toString(),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )

                    VendorSummaryCard(
                        title = "My Products",
                        value = vendorProducts.size.toString(),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Active orders heading
            item {
                VendorSectionHeader(
                    title = "Active Orders",
                    count = activeOrders.size,
                    expanded = ordersExpanded,
                    onClick = {
                        ordersExpanded = !ordersExpanded
                    }
                )
            }

            if (ordersExpanded) {
                if (activeOrders.isEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() +
                                    slideInVertically { it / 6 }
                        ) {
                            VendorEmptyState(
                                title = "No active orders",
                                description = "New customer orders will appear here."
                            )
                        }
                    }
                } else {
                    items(
                        items = activeOrders,
                        key = { it.orderId }
                    ) { order ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() +
                                    slideInVertically { it / 8 }
                        ) {
                            VendorOrderItem(
                                order = order,
                                onStatusClick = {
                                    appViewModel.advanceOrderStatus(order)
                                }
                            )
                        }
                    }
                }
            }

            // Products heading
            item {
                VendorSectionHeader(
                    title = "My Products",
                    count = vendorProducts.size,
                    expanded = productsExpanded,
                    onClick = {
                        productsExpanded = !productsExpanded
                    }
                )
            }

            if (productsExpanded) {
                if (vendorProducts.isEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() +
                                    slideInVertically { it / 6 }
                        ) {
                            VendorEmptyState(
                                title = "No products yet",
                                description = "Add your first product using the + button."
                            )
                        }
                    }
                } else {
                    items(
                        items = vendorProducts,
                        key = { it.productId }
                    ) { product ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() +
                                    slideInVertically { it / 8 }
                        ) {
                            VendorFoodItem(
                                product = product,
                                onClick = {
                                    onFoodClick(product.productId)
                                },
                                viewModel = appViewModel
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(72.dp))
            }
        }
    }
}

// ---------------- SUMMARY CARD ----------------

@Composable
fun VendorSummaryCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier.animateContentSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(
                    modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }

            AppText(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            AppText(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ---------------- COLLAPSIBLE SECTION HEADER ----------------

@Composable
fun VendorSectionHeader(
    title: String,
    count: Int,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 15.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                AppText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                AppText(
                    text = "$count ${if (count == 1) "item" else "items"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = if (expanded) {
                    Icons.Default.ExpandLess
                } else {
                    Icons.Default.ExpandMore
                },
                contentDescription = if (expanded) {
                    "Collapse $title"
                } else {
                    "Expand $title"
                },
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ---------------- EMPTY STATE ----------------

@Composable
fun VendorEmptyState(
    title: String,
    description: String
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppText(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            AppText(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ---------------- ORDER ITEM ----------------

@Composable
fun VendorOrderItem(
    order: OrderEntity,
    onStatusClick: () -> Unit
) {
    val status = order.status.trim().uppercase()

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    AppText(
                        text = "Order",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    AppText(
                        text = order.orderId,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = when (status) {
                        "CANCELLED" ->
                            MaterialTheme.colorScheme.errorContainer

                        "COMPLETED" ->
                            MaterialTheme.colorScheme.secondaryContainer

                        else ->
                            MaterialTheme.colorScheme.primaryContainer
                    }
                ) {
                    AppText(
                        text = status.replace('_', ' '),
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = when (status) {
                            "CANCELLED" ->
                                MaterialTheme.colorScheme.onErrorContainer

                            "COMPLETED" ->
                                MaterialTheme.colorScheme.onSecondaryContainer

                            else ->
                                MaterialTheme.colorScheme.onPrimaryContainer
                        },
                        maxLines = 1
                    )
                }
            }

            AppText(
                text = formatOrderTime(Date(order.timestamp)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedButton(
                onClick = onStatusClick,
                enabled = status != "COMPLETED" &&
                        status != "CANCELLED",
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (status) {
                        "COMPLETED" -> "Order completed"
                        "CANCELLED" -> "Order cancelled"
                        else -> "Advance order status"
                    }
                )
            }
        }
    }
}

// ---------------- PRODUCT ITEM ----------------

@Composable
fun VendorFoodItem(
    product: ProductEntity,
    onClick: () -> Unit,
    viewModel: AppViewModel
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        onClick = onClick,
        contentPadding = PaddingValues(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProductImage(
                product = product,
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    AppText(
                        text = product.productName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    AppText(
                        text = if (product.isAvailable) {
                            "Tap to view product details"
                        } else {
                            "Currently unavailable"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.width(12.dp))

                AppText(
                    text = "RM %.2f".format(
                        Locale.getDefault(),
                        product.productPrice
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// ---------------- PRODUCT IMAGE ----------------

@Composable
fun ProductImage(
    product: ProductEntity,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    CachedImage(
        imageId = product.productImage,
        viewModel = viewModel,
        contentDescription = product.productName,
        modifier = modifier
    )
}

// ---------------- DATE FORMATTING ----------------

fun formatOrderTime(date: Date): String {
    return SimpleDateFormat(
        "d MMM yyyy, h:mm a",
        Locale.getDefault()
    ).format(date)
}