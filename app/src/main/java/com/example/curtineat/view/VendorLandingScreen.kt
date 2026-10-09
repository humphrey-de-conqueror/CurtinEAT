package com.example.curtineat.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.SecondaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VendorLandingScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFoodClick: (String) -> Unit,
    onAddProductClick: () -> Unit
){
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
        showSearch = false,
        showNotifications = true,
        onProfileClick = onProfileClick,
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
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TextNormal(
                    text = currentVendor?.vendorName ?: "Vendor",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }

            /*
	     * ORDERS
	     */

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
                        TextNormal(
                            text = "No active orders",
                            fontSize = 16.sp
                        )
                    }
                } else {
                    items(
                        items = activeOrders,
                        key = { it.orderId }
                    ) { order ->
                        VendorOrderItem(
                            order = order,
                            onStatusClick = {
                                appViewModel.advanceOrderStatus(order)
                            }
                        )
                    }
                }
            }

            /*
	     * PRODUCTS
	     */

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
                        TextNormal(
                            text = "No products yet",
                            fontSize = 16.sp
                        )
                    }
                } else {
                    items(
                        items = vendorProducts,
                        key = { it.productId }
                    ) { product ->
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
    }
}

@Composable
fun VendorSectionHeader(
    title: String,
    count: Int,
    expanded: Boolean,
    onClick: () -> Unit
) {
    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextNormal(
                text = "$title ($count)",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Icon(
                imageVector = if (expanded) {
                    Icons.Default.ExpandLess
                } else {
                    Icons.Default.ExpandMore
                },
                contentDescription = if (expanded) {
                    "Collapse"
                } else {
                    "Expand"
                }
            )
        }
    }

}

@Composable
fun VendorOrderItem(
    order: OrderEntity,
    onStatusClick: () -> Unit
) {
    SecondaryCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        TextNormal(
            text = "Order ID: ${order.orderId}",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            maxLines = 1
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        TextNormal(
            text = formatOrderTime(Date(order.timestamp)),
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = onStatusClick,
            enabled = order.status.trim().uppercase() != "COMPLETED"
        ) {
            Text(
                text = order.status.uppercase()
            )
        }
    }

}

fun formatOrderTime(date: Date): String {
    return SimpleDateFormat(
        "d MMM yyyy, h:mm a",
        Locale.getDefault()
    ).format(date)
}

@Composable
fun VendorFoodItem(
    product: ProductEntity,
    onClick: () -> Unit,
    viewModel: AppViewModel
) {
    SecondaryCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        contentPadding = PaddingValues(12.dp)
    ) {
        ProductImage(
            product = product,
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )

        mySpacer()

        TextNormal(
            text = product.productName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        TextNormal(
            text = "RM %.2f".format(product.productPrice),
            fontSize = 16.sp
        )
    }
}

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
