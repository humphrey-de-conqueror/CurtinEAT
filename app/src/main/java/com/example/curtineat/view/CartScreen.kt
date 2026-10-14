package com.example.curtineat.view

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import com.example.curtineat.model.CartItem
import com.example.curtineat.ui.components.AppBackButton
import com.example.curtineat.ui.components.AppCard
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.ui.theme.AppThemeMode
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import kotlinx.coroutines.delay

// ---------------- CART SCREEN ----------------

@Composable
fun CartScreen(
    appViewModel: AppViewModel,
    onBackButtonClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit,
    themeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit
) {
    val cart by appViewModel.cart.collectAsState()
    val customerState by appViewModel.customerState.collectAsState()
    val vendorState by appViewModel.vendorState.collectAsState()
    val authenticationState by appViewModel.authenticationState.collectAsState()
    val checkoutCompleted by appViewModel.checkoutCompleted.collectAsState()
    val checkoutInProgress by appViewModel.checkoutInProgress.collectAsState()
    val checkoutError by appViewModel.checkoutError.collectAsState()

    val loggedInCustomerId = authenticationState.userId
        .takeIf { authenticationState.role == UserRole.CUSTOMER }

    val currentCustomer = customerState.data
    val vendorId = cart.firstOrNull()?.product?.vendorId

    val cartVendor = vendorState.data.find {
        it.vendorId == vendorId
    }

    var showEmptyCartDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(loggedInCustomerId) {
        if (loggedInCustomerId != null) {
            appViewModel.observeCustomer(loggedInCustomerId)
        }
    }

    LaunchedEffect(checkoutCompleted) {
        if (checkoutCompleted) {
            delay(1000)
            appViewModel.clearCheckoutCompleted()
            onBackButtonClick()
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (cart.isEmpty()) {
                EmptyCart(
                    onBackButtonClick = onBackButtonClick,
                    checkoutCompleted = checkoutCompleted
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Customer information
//                    AppCard(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentPadding = PaddingValues(16.dp)
//                    ) {
//                        AppText(
//                            text = "Customer information",
//                            style = androidx.compose.material3.MaterialTheme
//                                .typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold
//                        )
//
//                        Spacer(Modifier.height(8.dp))
//
//                        if (loggedInCustomerId != null) {
//                            AppText(
//                                text = "Customer ID: $loggedInCustomerId",
//                                style = androidx.compose.material3.MaterialTheme
//                                    .typography.bodyMedium,
//                                color = androidx.compose.material3.MaterialTheme
//                                    .colorScheme.onSurfaceVariant
//                            )
//
//                            AppText(
//                                text = "Ordering as: ${
//                                    currentCustomer?.customerName
//                                        ?: "Loading customer..."
//                                }",
//                                style = androidx.compose.material3.MaterialTheme
//                                    .typography.bodyMedium
//                            )
//                        } else {
//                            AppText(
//                                text = "You are not logged in. Log in before placing your order.",
//                                style = androidx.compose.material3.MaterialTheme
//                                    .typography.bodyMedium,
//                                color = androidx.compose.material3.MaterialTheme
//                                    .colorScheme.onSurfaceVariant
//                            )
//                        }
//                    }

                    // Vendor header
                    CartHeader(
                        vendorName = cartVendor?.vendorName
                            ?: "Restaurant",
                        location = cartVendor?.location.orEmpty(),
                        onBackButtonClick = onBackButtonClick
                    )

                    // Cart items
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppText(
                            text = "Your items",
                            style = androidx.compose.material3.MaterialTheme
                                .typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        cart.forEach { cartItem ->
                            CartItemRow(
                                cartItem = cartItem,
                                appViewModel = appViewModel,
                                onIncrease = {
                                    appViewModel.increaseQuantity(
                                        cartItem.product.productId
                                    )
                                },
                                onDecrease = {
                                    if (
                                        cart.size == 1 &&
                                        cartItem.quantity == 1
                                    ) {
                                        showEmptyCartDialog = true
                                    } else {
                                        appViewModel.decreaseQuantity(
                                            cartItem.product.productId
                                        )
                                    }
                                }
                            )
                        }
                    }

                    // Payment summary
                    CartPaymentSummary(
                        totalPrice = appViewModel.cartTotalPrice()
                    )

                    // Checkout error
                    if (checkoutError != null) {
                        AppCard(
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = androidx.compose.material3.MaterialTheme
                                .colorScheme.errorContainer
                        ) {
                            AppText(
                                text = checkoutError.orEmpty(),
                                color = androidx.compose.material3.MaterialTheme
                                    .colorScheme.onErrorContainer,
                                style = androidx.compose.material3.MaterialTheme
                                    .typography.bodyMedium
                            )

                            TextButton(
                                onClick = {
                                    appViewModel.clearCheckoutError()
                                }
                            ) {
                                AppText(
                                    text = "Dismiss",
                                    color = androidx.compose.material3.MaterialTheme
                                        .colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    // Checkout action
                    Button(
                        onClick = {
                            if (loggedInCustomerId == null) {
                                onLoginClick()
                            } else {
                                appViewModel.checkout(
                                    customerId = loggedInCustomerId
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !checkoutInProgress,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (checkoutInProgress) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )

                            Spacer(Modifier.size(8.dp))

                            AppText(text = "Placing Order...")
                        } else {
                            AppText(
                                text = if (loggedInCustomerId == null) {
                                    "Log in to Place Order"
                                } else {
                                    "Place Order"
                                },
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }

    // Confirm removal of the final cart item
    if (showEmptyCartDialog) {
        AlertDialog(
            onDismissRequest = {
                showEmptyCartDialog = false
            },
            title = {
                AppText(
                    text = "Remove last item?",
                    style = androidx.compose.material3.MaterialTheme
                        .typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                AppText(
                    text = "Removing this item will empty your cart.",
                    style = androidx.compose.material3.MaterialTheme
                        .typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showEmptyCartDialog = false
                        appViewModel.clearCart()
                        onBackButtonClick()
                    }
                ) {
                    AppText(text = "Remove")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEmptyCartDialog = false
                    }
                ) {
                    AppText(text = "Cancel")
                }
            }
        )
    }
}

// ---------------- EMPTY CART ----------------

@Composable
fun EmptyCart(
    onBackButtonClick: () -> Unit,
    checkoutCompleted: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppText(
                text = if (checkoutCompleted) {
                    "Order placed successfully!"
                } else {
                    "Your cart is empty"
                },
                style = androidx.compose.material3.MaterialTheme.typography
                    .headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            if (!checkoutCompleted) {
                AppText(
                    text = "Add food from a restaurant to get started.",
                    style = androidx.compose.material3.MaterialTheme.typography
                        .bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme
                        .onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = onBackButtonClick,
                shape = RoundedCornerShape(12.dp)
            ) {
                AppText(
                    text = if (checkoutCompleted) {
                        "Continue"
                    } else {
                        "Back to restaurants"
                    }
                )
            }
        }
    }
}

// ---------------- CART HEADER ----------------

@Composable
fun CartHeader(
    vendorName: String,
    location: String,
    onBackButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBackButton(
            onClick = onBackButtonClick
        )

        Spacer(Modifier.size(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            AppText(
                text = vendorName,
                style = androidx.compose.material3.MaterialTheme.typography
                    .headlineSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )

            AppText(
                text = location.ifBlank { "Location not provided" },
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ---------------- CART ITEM ----------------

@Composable
fun CartItemRow(
    cartItem: CartItem,
    appViewModel: AppViewModel,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CachedImage(
                imageId = cartItem.product.productImage,
                viewModel = appViewModel,
                contentDescription = cartItem.product.productName,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AppText(
                    text = cartItem.product.productName,
                    style = androidx.compose.material3.MaterialTheme.typography
                        .titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                AppText(
                    text = "RM %.2f".format(
                        cartItem.product.productPrice
                    ),
                    style = androidx.compose.material3.MaterialTheme.typography
                        .bodyLarge,
                    color = androidx.compose.material3.MaterialTheme.colorScheme
                        .primary
                )

                AppText(
                    text = "Subtotal: RM %.2f".format(
                        cartItem.product.productPrice * cartItem.quantity
                    ),
                    style = androidx.compose.material3.MaterialTheme.typography
                        .bodySmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme
                        .onSurfaceVariant
                )
            }

            Spacer(Modifier.size(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease quantity"
                    )
                }

                AppText(
                    text = cartItem.quantity.toString(),
                    style = androidx.compose.material3.MaterialTheme.typography
                        .titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase quantity"
                    )
                }
            }
        }
    }
}

// ---------------- PAYMENT SUMMARY ----------------

@Composable
fun CartPaymentSummary(
    totalPrice: Double
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        AppText(
            text = "Payment",
            style = androidx.compose.material3.MaterialTheme.typography
                .titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(8.dp))

        AppText(
            text = "Pay physically at the store when collecting your order.",
            style = androidx.compose.material3.MaterialTheme.typography
                .bodyMedium,
            color = androidx.compose.material3.MaterialTheme.colorScheme
                .onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = "Total",
                style = androidx.compose.material3.MaterialTheme.typography
                    .titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            AppText(
                text = "RM %.2f".format(totalPrice),
                style = androidx.compose.material3.MaterialTheme.typography
                    .titleLarge,
                color = androidx.compose.material3.MaterialTheme.colorScheme
                    .primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}