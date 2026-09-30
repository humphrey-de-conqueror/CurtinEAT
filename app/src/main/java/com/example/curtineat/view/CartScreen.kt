package com.example.curtineat.view

import android.R.attr.contentDescription
import android.R.attr.fontWeight
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
import androidx.compose.material.icons.filled.ChevronLeft
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


// ---------------- CART SCREEN ----------------
@Composable
fun CartScreen(
    appViewModel: AppViewModel,
    onBackButtonClick: () -> Unit,
    onHomeClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val vendorId = appViewModel.cart
        .firstOrNull()
        ?.product
        ?.vendorID

    val cartVendor = appViewModel.vendor.find {
        it.vendorId == vendorId
    }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
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
                    onBackButtonClick = onBackButtonClick
                )

            } else {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Vendor
                    CartHeader(
                        vendorName = cartVendor?.vendorName ?: "",
                        distance = cartVendor?.distance ?: 0.0,
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
                                appViewModel.decreaseQuantity(
                                    cartItem.product.productId
                                )
                            }
                        )

                        mySpacer()
                    }

                    // Payment summary
                    CartPaymentSummary(
                        totalPrice = appViewModel.cartTotalPrice()
                    )

                    mySpacer()

                    PrimaryButton(
                        text = "Checkout",
                        onClick = {
                            // no function yet
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


// ---------------- EMPTY CART ----------------
@Composable
fun EmptyCart(
    onBackButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        BackButton(
            onClick = onBackButtonClick,
            modifier = Modifier.align(Alignment.TopStart)
        )

        TextNormal(
            text = "Your cart is empty",
            modifier = Modifier.align(Alignment.Center)
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
    totalPrice: Double
) {
    // Hardcoded wallet balance for now
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextNormal(
                text = "Wallet Balance",
                fontSize = 16.sp
            )
            mySpacerWidth()
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = "Wallet",
                modifier = Modifier.size(20.dp)
            )
        }
//
//        TextNormal(
//            text = "RM %.2f".format(customer.walletBalance),
//            fontWeight = FontWeight.Bold
//        )
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