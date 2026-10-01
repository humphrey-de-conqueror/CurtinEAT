package com.example.curtineat.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.model.CartItem
import com.example.curtineat.ui.theme.BackButton
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.SecondaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
import com.example.curtineat.ui.theme.mySpacerWidth
import com.example.curtineat.viewmodel.AppViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay


// ---------------- CART SCREEN ----------------
@Composable
fun CartScreen(
    // since cardScreen can see humburger, expect full onAction
    appViewModel: AppViewModel,
    onBackButtonClick: () -> Unit,
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit
) {
//    val loggedInCustomerId: Int? = 1 // hardcoded change to null if want

    val loggedInCustomerId = appViewModel.account.customerId

    var showEmptyCartDialog by remember {
        mutableStateOf(false)
    }

    val currentCustomer = appViewModel.customer.find {
        it.customerId == loggedInCustomerId
    }

    val moneyBalance = currentCustomer?.moneyBalance ?: 0.0

    val vendorId = appViewModel.cart
        .firstOrNull()
        ?.product
        ?.vendorID

    val cartVendor = appViewModel.vendor.find {
        it.vendorId == vendorId
    }

    LaunchedEffect(appViewModel.checkoutCompleted) {

        if (appViewModel.checkoutCompleted) {

            delay(1000)

            onBackButtonClick()
        }
    }


    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        showSearch = false,
        showNotifications = true
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {


            if (appViewModel.cart.isEmpty()) {

                EmptyCart(
                    onBackButtonClick = onBackButtonClick,
                    checkoutCompleted = appViewModel.checkoutCompleted
                )

            } else {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    //----
                    mySpacer()
                    if (loggedInCustomerId != null) {
                        TextNormal(
                            text = "Customer ID: $loggedInCustomerId",
                        )

                        TextNormal(
                            text = "Ordering as: ${currentCustomer?.customerName ?: ""}",

                            )
                    } else {
                        TextNormal(
                            text = "Not logged in",
                        )
                    }
                    mySpacer()
                    //----

                    // Vendor
                    CartHeader(
                        vendorName = cartVendor?.vendorName ?: "",
                        distance = cartVendor?.distance ?: 0.0,
                        //testing
//                        moneyBalance = cartVendor?.moneyBalance ?: 0.0,
                        onBackButtonClick = onBackButtonClick
                    )

                    mySpacer()

                    // Cart items
                    appViewModel.cart.forEach { cartItem ->

                        CartItemRow(
                            cartItem = cartItem,
                            onIncrease = {
                                appViewModel.increaseQuantity(
                                    cartItem.product.productId
                                )
                            },
                            onDecrease = {

                                if (
                                    appViewModel.cart.size == 1 &&
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
                        moneyBalance = moneyBalance,
                        totalPrice = appViewModel.cartTotalPrice(),
                        onWalletClick = {
                            if (loggedInCustomerId == null) {
                                onLoginClick()
                            } else {
                                onWalletClick()
                            }
                        }
                    )

                    mySpacer()

                    PrimaryButton(
                        text = "Checkout",
                        onClick = {

                            when {

                                // Not logged in
                                loggedInCustomerId == null -> {
                                    onLoginClick()
                                }

                                // Not enough money
                                moneyBalance < appViewModel.cartTotalPrice() -> {
                                    onWalletClick()
                                }

                                // Enough money
                                else -> {
                                    appViewModel.checkout(
                                        customerId = loggedInCustomerId,
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
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
                    TextNormal(
                        text = "Remove"
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showEmptyCartDialog = false
                    }
                ) {
                    TextNormal(
                        text = "Cancel"
                    )
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
//    moneyBalance: Double, //Testing
    onBackButtonClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        BackButton(
            onClick = onBackButtonClick
        )

        Column {
            TextNormal(
                text = vendorName,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            TextNormal(
                text = "$distance km away",
                fontSize = 14.sp
            )

            //testing
//            TextNormal(
//                text = "Vendor Balance: RM %.2f".format(moneyBalance),
//                fontSize = 14.sp
//            )
        }
    }
}


// ---------------- CART ITEM ----------------
@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val imageRes = getDrawableId(
        cartItem.product.productImage
    )

    SecondaryCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Food image
            Image(
                painter = painterResource(imageRes),
                contentDescription = cartItem.product.productName,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
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
    moneyBalance: Double,
    totalPrice: Double,
    onWalletClick: () -> Unit
) {

    SecondaryCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp),
        onClick = onWalletClick
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Wallet",
                    modifier = Modifier.size(22.dp)
                )

                mySpacerWidth()

                Column {
                    TextNormal(
                        text = "Wallet Balance",
                        fontSize = 14.sp
                    )

                    TextNormal(
                        text = "RM %.2f".format(moneyBalance),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open wallet"
            )
        }
    }

    mySpacer()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextNormal(
            text = "Total",
            fontWeight = FontWeight.Bold
        )

        TextNormal(
            text = "RM %.2f".format(totalPrice),
            fontWeight = FontWeight.Bold
        )
    }
}