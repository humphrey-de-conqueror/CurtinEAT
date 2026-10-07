package com.example.curtineat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderData
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OrderHistoryScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onHistoryClick: () -> Unit
) {

    val orders by appViewModel.orders.collectAsState()
    val vendors by appViewModel.vendors.collectAsState()
    val customers by appViewModel.customers.collectAsState()

    var role by remember { mutableStateOf<UserRole?>(null) }

    val isLoggedIn = role == UserRole.CUSTOMER || role == UserRole.VENDOR
    val isVendorAccount = role == UserRole.VENDOR

    //see acc type
    LaunchedEffect(Unit) {
        appViewModel.checkCurrentUserRole { role = it }
    }

    // (uid + acc type) = their unique history
    LaunchedEffect(role) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            when (role) {

                UserRole.CUSTOMER -> {
                    appViewModel.startCustomerOrderListener(uid)
                    // vendor names are shown on the customer's cards
                    if (vendors.isEmpty()) appViewModel.loadVendors()
                }

                UserRole.VENDOR -> {
                    appViewModel.startVendorOrderListener(uid)
                    // customer names are shown on the vendor's cards
                    if (customers.isEmpty()) appViewModel.loadCustomers()
                }

                else -> {} //display message saying log in to see account history
            }
        }
    }

    val sortedOrders = orders.sortedByDescending {
        it.timestamp.toDate()
    }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onProfileClick = onProfileClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        onHistoryClick = onHistoryClick,
        showSearch = false,
        showNotifications = true
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

                    itemsIndexed(sortedOrders) { _, order ->

                        // customer sees the vendor, vendor sees the customer
                        val counterpartLabel =
                            if (isVendorAccount) {
                                customers.firstOrNull {
                                    it.customerId == order.customerId
                                }?.customerName ?: "Customer"
                            } else {
                                vendors.firstOrNull {
                                    it.vendorId == order.vendorId
                                }?.vendorName ?: "Vendor"
                            }

                        OrderHistoryCard(
                            order = order,
                            counterpartLabel = counterpartLabel
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

            // PLACEHOLDER: will be used for sorting later
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

// Read-only: no click handling on purpose
@Composable
fun OrderHistoryCard(
    order: FirebaseOrderData,
    counterpartLabel: String
) {

    PrimaryCard(
        modifier = Modifier.fillMaxWidth()
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

            Text(
                text = order.status,
                color = orderStatusColor(order.status),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        // DATE
        TextNormal(
            text = formatOrderDateTime(order.timestamp.toDate()),
            fontSize = 12.sp
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // PRODUCTS
        order.products.forEach { product ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextNormal(
                    text = "${product.quantity} x ${product.productName}",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp
                )

                TextNormal(
                    text = "RM %.2f".format(
                        product.productPrice * product.quantity
                    ),
                    fontSize = 14.sp
                )
            }
        }

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

    return when (status.uppercase()) {
        "PENDING" -> Color(0xFFEA7422)
        "PREPARING" -> Color(0xFF1565C0)
        "READY" -> Color(0xFF00897B)
        "COMPLETED" -> Color(0xFF2E7D32)
        "CANCELLED" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

fun formatOrderDateTime(
    date: java.util.Date
): String {

    return java.text.SimpleDateFormat(
        "d MMM yyyy, h:mm a",
        java.util.Locale.getDefault()
    ).format(date)
}