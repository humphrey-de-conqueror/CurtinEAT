package com.example.curtineat.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.SecondaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
import com.example.curtineat.view.AppScaffold
import com.example.curtineat.view.getDrawableId
import com.example.curtineat.viewmodel.AppViewModel


//package com.example.curtineat.view
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.room.util.TableInfo
//import com.example.curtineat.database.Order
//import com.example.curtineat.database.OrderItem
//import com.example.curtineat.database.OrderItemDao
//import com.example.curtineat.viewmodel.AppViewModel
//
//@Composable
//fun CartScreen(
//    appViewModel: AppViewModel,
//    onBackButtonClick: () -> Unit,
//    onHomeClick: () -> Unit
//) {
//    val vendorId = appViewModel.cart
//        .firstOrNull()
//        ?.product
//        ?.vendorID
//
//    val cartVendor = appViewModel.vendor.find {
//        it.vendorId == vendorId
//    }
//    val totalPrice = appViewModel.cartTotalPrice()
//    val order = Order(totalPrice = totalPrice)
//    appViewModel.insertOrder(order)
//
//    AppScaffold(
//        appViewModel = appViewModel,
//        onHomeClick = onHomeClick,
//        title = "Order Summary",
//        showSearch = false,
//        showNotifications = true
//    ) { innerPadding ->
//
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .padding(16.dp)
//        ) {
//            Text(text = "This is cart screen")
//
//            appViewModel.cart.forEach { eachProduct ->
//                Text(text = eachProduct.product.productName)
//            }
//
//            Button(
//                onClick = {
//                    val totalPrice = appViewModel.cartTotalPrice()
//                    val order = Order(totalPrice = totalPrice)
//                    appViewModel.insertOrder(order)
//                    appViewModel.cart.forEach { eachProduct ->
//                        val orderItem = OrderItem(
//                            orderId = order.orderId,
//                            productId = eachProduct.product.productId,
//                            quantity = eachProduct.quantity
//                            )
//
//                        appViewModel.insertOrderItem(orderItem)
//                    }
//                }
//            ) { Text(text = "Confirm order & Submit") }
//
//            Button(
//                onClick = onBackButtonClick
//            ) {
//                Text(text = "Back")
//            }
//
//            // pls remove in production
//            Button(
//                onClick = {
////                    appViewModel.getTempOrder()
//                }
//            ) {
//                Text("Take a look at order/order item database")
//            }
//
//            appViewModel.tempOrder.forEach { eachOrder ->
//                Text(text = (eachOrder.orderId - 1).toString())
//            }
//
//            appViewModel.tempOrderItem.forEach { eachOrderItem ->
//                Text(
//                    text = "${eachOrderItem.orderId} | ${eachOrderItem.productId}"
//                )
//            }
//        }
//    }
//}

@Composable
fun CartScreen(
    appViewModel: AppViewModel,
    onBackButtonClick: () -> Unit,
    onHomeClick: () -> Unit
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

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            if (appViewModel.cart.isEmpty()) {

                TextNormal(
                    text = "Your cart is empty"
                )

            } else {

                TextNormal(
                    text = cartVendor?.vendorName ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                TextNormal(
                    text = "${cartVendor?.distance ?: 0.0} km away",
                    fontSize = 14.sp
                )

                mySpacer()

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

                TextNormal(
                    text = "Total: RM %.2f".format(
                        appViewModel.cartTotalPrice()
                    ),
                    fontWeight = FontWeight.Bold
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

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val imageRes = getDrawableId(cartItem.product.productImage)

    SecondaryCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(imageRes),
                contentDescription = cartItem.product.productName,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

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
                    text = "RM %.2f".format(cartItem.product.productPrice),
                    fontSize = 16.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrease) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease"
                    )
                }

                TextNormal(
                    text = cartItem.quantity.toString(),
                    fontSize = 16.sp
                )

                IconButton(onClick = onIncrease) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase"
                    )
                }
            }
        }
    }
}