package com.example.curtineat.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.curtineat.database.Order
import com.example.curtineat.database.OrderItem
import com.example.curtineat.database.OrderItemDao
import com.example.curtineat.viewmodel.AppViewModel

@Composable
fun CartScreen(
    appViewModel: AppViewModel,
    onBackButtonClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        showSearch = false,
        showNotifications = true
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "This is cart screen")

            appViewModel.cart.forEach { eachProduct ->
                Text(text = eachProduct.productName)
            }

            Button(
                onClick = {
                    val totalPrice = appViewModel.cartTotalPrice()
                    val order = Order(totalPrice = totalPrice)
                    appViewModel.insertOrder(order)
                    appViewModel.cart.forEach { eachProduct ->
                        val orderItem = OrderItem(
                            orderId = order.orderId,
                            productId = eachProduct.productId,
                            quantity = 2 //place holder
                            )

                        appViewModel.insertOrderItem(orderItem)
                    }
                }
            ) { Text(text = "Confirm order & Submit") }

            Button(
                onClick = onBackButtonClick
            ) {
                Text(text = "Back")
            }

            // pls remove in production
            Button(
                onClick = {
                    appViewModel.getTempOrder()
                }
            ) {
                Text("Take a look at order/order item database")
            }

            appViewModel.tempOrder.forEach { eachOrder ->
                Text(text = (eachOrder.orderId - 1).toString())
            }

            appViewModel.tempOrderItem.forEach { eachOrderItem ->
                Text(
                    text = "${eachOrderItem.orderId} | ${eachOrderItem.productId}"
                )
            }
        }
    }
}