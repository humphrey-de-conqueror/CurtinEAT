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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.curtineat.ui.theme.BackButton
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.SecondaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
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
    onHistoryClick: () -> Unit
){
    val cart by appViewModel.cart.collectAsState()
    val customerState by appViewModel.customerState.collectAsState()
    val vendorState by appViewModel.vendorState.collectAsState()
    val authenticationState by appViewModel.authenticationState.collectAsState()
    val checkoutCompleted by appViewModel.checkoutCompleted.collectAsState()
    val checkoutInProgress by appViewModel.checkoutInProgress.collectAsState()
    val checkoutError by appViewModel.checkoutError.collectAsState()

    val loggedInCustomerId = authenticationState.userId
        .takeIf {
            authenticationState.role == UserRole.CUSTOMER
        }

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
        showNotifications = true
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (cart.isEmpty()) {

                EmptyCart(
                    onBackButtonClick = onBackButtonClick,
                    checkoutCompleted = checkoutCompleted
                )

            } else {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Customer information
                    mySpacer()

                    if (loggedInCustomerId != null) {
                        TextNormal(
                            text = "Customer ID: $loggedInCustomerId"
                        )

                        TextNormal(
                            text = "Ordering as: ${currentCustomer?.customerName ?: ""}"
                        )
                    } else {
                        TextNormal(
                            text = "Not logged in"
                        )
                    }

                    mySpacer()

                    // Vendor
                    CartHeader(
                        vendorName = cartVendor?.vendorName ?: "",
                        distance = cartVendor?.distance ?: 0.0,
                        onBackButtonClick = onBackButtonClick
                    )

                    mySpacer()

                    // Cart items
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

                        mySpacer()
                    }

                    // Payment summary
                    CartPaymentSummary(
                        totalPrice = appViewModel.cartTotalPrice()
                    )

                    mySpacer()

                    if (checkoutError != null) {
                        TextNormal(
                            text = checkoutError ?: "",
                        )

                        TextButton(
                            onClick = {
                                appViewModel.clearCheckoutError()
                            }
                        ) {
                            TextNormal(text = "Dismiss")
                        }
                    }

                    PrimaryButton(
                        text = if (checkoutInProgress) {
                            "Placing Order..."
                        } else {
                            "Place Order"
                        },
                        onClick = {
                            if (loggedInCustomerId == null) {
                                onLoginClick()
                            } else {
                                appViewModel.checkout(
                                    customerId = loggedInCustomerId
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (checkoutInProgress) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 8.dp)
                                .size(24.dp)
                        )
                    }
                }
            }
        }
    }

    if (showEmptyCartDialog) {
        AlertDialog(
            onDismissRequest = {
                showEmptyCartDialog = false
            },
            title = {
                TextNormal(
                    text = "Remove last item?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                TextNormal(
                    text = "Removing this item will empty your cart."
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
                    TextNormal(text = "Remove")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEmptyCartDialog = false
                    }
                ) {
                    TextNormal(text = "Cancel")
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
        modifier = Modifier.fillMaxSize()
    ) {

        BackButton(
            onClick = onBackButtonClick,
            modifier = Modifier.align(Alignment.TopStart)
        )

        TextNormal(
            text = if (checkoutCompleted) {
                "Order placed successfully!"
            } else {
                "Your cart is empty"
            },
            modifier = Modifier.align(Alignment.Center),
            fontWeight = if (checkoutCompleted) {
                FontWeight.Bold
            } else {
                null
            },
            fontSize = if (checkoutCompleted) {
                22.sp
            } else {
                20.sp
            }
        )
    }
}

// ---------------- CART HEADER ----------------
@Composable
fun CartHeader(
    vendorName: String,
    distance: Double,
    onBackButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BackButton(
            onClick = onBackButtonClick
        )

        Column(
            modifier = Modifier.padding(start = 0.dp)
        ) {
            TextNormal(
                text = vendorName,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            TextNormal(
                text = "$distance km away",
                fontSize = 14.sp
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
    SecondaryCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Food image
            CachedImage(
                imageId = cartItem.product.productImage,
                viewModel = appViewModel,
                contentDescription = cartItem.product.productName,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            // Food information
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {

                TextNormal(
                    text = cartItem.product.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                TextNormal(
                    text = "RM %.2f".format(
                        cartItem.product.productPrice
                    ),
                    fontSize = 16.sp
                )
            }

            // Quantity
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onDecrease
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease"
                    )
                }

                TextNormal(
                    text = cartItem.quantity.toString(),
                    fontSize = 16.sp
                )

                IconButton(
                    onClick = onIncrease
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase"
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

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        TextNormal(
            text = "Payment",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        TextNormal(
            text = "Pay physically at the store when collecting your order.",
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextNormal(
                text = "Total",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            TextNormal(
                text = "RM %.2f".format(totalPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}