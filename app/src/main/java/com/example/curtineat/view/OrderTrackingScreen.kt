package com.example.curtineat.view

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.OrderProductEntity
import com.example.curtineat.viewmodel.AppViewModel

private val orderStages = listOf(
    "Received",
    "Preparing",
    "Ready for pick up"
)

@Composable
fun OrderTrackingScreen(
    appViewModel: AppViewModel,
    orderId: String,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLogInClick: () -> Unit = {},
    onHistoryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var order by remember(orderId) {
        mutableStateOf<OrderEntity?>(null)
    }

    var products by remember(orderId) {
        mutableStateOf<List<OrderProductEntity>>(emptyList())
    }

    var isLoading by remember(orderId) {
        mutableStateOf(true)
    }

    var loadFailed by remember(orderId) {
        mutableStateOf(false)
    }

    LaunchedEffect(orderId) {
        isLoading = true
        loadFailed = false

        appViewModel.loadOrderDetails(orderId) { loadedOrder, loadedProducts ->
            order = loadedOrder
            products = loadedProducts
            loadFailed = loadedOrder == null
            isLoading = false
        }
    }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onProfileClick = onProfileClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLogInClick,
        onHistoryClick = onHistoryClick,
        showSearch = false,
        showNotifications = false
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Order tracking",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Order ID: $orderId",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(Modifier.height(20.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                loadFailed || order == null -> {
                    Text(
                        text = "Unable to load this order.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                else -> {
                    val currentOrder = order!!
                    val currentStage = getOrderStage(currentOrder.status)

                    OrderProgressBar(
                        currentStage = currentStage
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = currentOrder.status,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = when (currentStage) {
                            0 -> "Your order has been received."
                            1 -> "Your food is being prepared."
                            2 -> "Your food is ready for pick up."
                            else -> "Current order status: ${currentOrder.status}"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Items in this order",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    if (products.isEmpty()) {
                        Text("No product details found for this order.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = products,
                                key = { it.localId }
                            ) { product ->
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement =
                                            Arrangement.SpaceBetween,
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = product.productName,
                                                style = MaterialTheme.typography.bodyLarge
                                            )

                                            Text(
                                                text = "RM %.2f each".format(
                                                    product.productPrice
                                                ),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                        Text(
                                            text = "x${product.quantity}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }

                            item {
                                Spacer(Modifier.height(8.dp))

                                HorizontalDivider()

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement =
                                        Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = "RM %.2f".format(
                                            currentOrder.totalPrice
                                        ),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getOrderStage(status: String): Int {
    return when (status.trim().lowercase()) {
        "received", "pending", "placed" -> 0
        "preparing", "in progress" -> 1
        "ready for pick up", "ready for pickup", "ready" -> 2
        else -> -1
    }
}

@Composable
fun OrderProgressBar(
    currentStage: Int,
    modifier: Modifier = Modifier
) {
    val active = MaterialTheme.colorScheme.primary
    val inactive = MaterialTheme.colorScheme.surfaceVariant

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        orderStages.forEachIndexed { index, label ->
            val reached = currentStage >= index && currentStage >= 0

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                if (index == 0) {
                                    androidx.compose.ui.graphics.Color.Transparent
                                } else if (currentStage >= index) {
                                    active
                                } else {
                                    inactive
                                }
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (reached) active else inactive),
                        contentAlignment = Alignment.Center
                    ) {
                        if (index < currentStage) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                color = if (reached) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                if (index == orderStages.lastIndex) {
                                    androidx.compose.ui.graphics.Color.Transparent
                                } else if (currentStage > index) {
                                    active
                                } else {
                                    inactive
                                }
                            )
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}