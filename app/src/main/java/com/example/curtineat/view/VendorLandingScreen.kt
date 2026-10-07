package com.example.curtineat.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.data.remote.firebase.model.FirebaseProductData
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
import com.example.curtineat.viewmodel.AppViewModel
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.OutlinedButton
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderData
import com.example.curtineat.ui.theme.SecondaryCard
import androidx.compose.material3.Text
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.google.firebase.auth.FirebaseAuth



@Composable
fun VendorLandingScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onFoodClick: (String) -> Unit,
    onAddProductClick: () -> Unit
) {

    val vendors by appViewModel.vendors.collectAsState()

    val products by appViewModel.products.collectAsState()

    val orders by appViewModel.orders.collectAsState()

    val loggedInVendorId = FirebaseAuth.getInstance().currentUser?.uid

    val context = LocalContext.current

    var ordersExpanded by
    rememberSaveable {
        mutableStateOf(true)
    }

    var productsExpanded by
    rememberSaveable {
        mutableStateOf(true)
    }

    val currentVendor =
        vendors.find {
            it.vendorId == loggedInVendorId
        }

    val vendorProducts =
        products.filter {
            it.vendorId == loggedInVendorId
        }

    val activeOrders =
        orders
            .filter {
                it.vendorId == loggedInVendorId &&
                        it.status.uppercase() != "COMPLETED"
            }
            .sortedByDescending {
                it.timestamp.toDate()
            }


    LaunchedEffect(
        loggedInVendorId
    ) {

        if (loggedInVendorId != null) {

            appViewModel.loadHomeData()

            // Realtime incoming orders
            appViewModel
                .startVendorOrderListener(
                    loggedInVendorId
                )

            // Realtime notification drawer
            appViewModel
                .startNotificationListener(
                    loggedInVendorId
                )
        }
    }


    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        showSearch = false,
        showNotifications = true,
        onProfileClick = onProfileClick,

        floatingActionButton = {

            FloatingActionButton(
                onClick = onAddProductClick
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Add,
                    contentDescription =
                        "Add product"
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            item {

                TextNormal(
                    text =
                        currentVendor
                            ?.vendorName
                            ?: "Vendor",
                    fontWeight =
                        FontWeight.Bold,
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
                        ordersExpanded =
                            !ordersExpanded
                    }
                )
            }


            if (ordersExpanded) {

                if (activeOrders.isEmpty()) {

                    item {

                        TextNormal(
                            text =
                                "No active orders",
                            fontSize = 16.sp
                        )
                    }

                } else {

                    items(
                        items = activeOrders,
                        key = {
                            it.orderId
                        }
                    ) { order ->

                        VendorOrderItem(
                            order = order,
                            onStatusClick = {

                                appViewModel
                                    .advanceOrderStatus(
                                        order
                                    )
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
                    count =
                        vendorProducts.size,
                    expanded =
                        productsExpanded,
                    onClick = {
                        productsExpanded =
                            !productsExpanded
                    }
                )
            }


            if (productsExpanded) {

                if (vendorProducts.isEmpty()) {

                    item {

                        TextNormal(
                            text =
                                "No products yet",
                            fontSize = 16.sp
                        )
                    }

                } else {

                    items(
                        items = vendorProducts,
                        key = {
                            it.productId
                        }
                    ) { product ->

                        VendorFoodItem(
                            product = product,
                            onClick = {
                                onFoodClick(
                                    product.productId
                                )
                            }
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
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            TextNormal(
                text = "$title ($count)",
                fontWeight =
                    FontWeight.Bold,
                fontSize = 22.sp
            )

            Icon(
                imageVector =
                    if (expanded) {
                        Icons.Default.ExpandLess
                    } else {
                        Icons.Default.ExpandMore
                    },
                contentDescription =
                    if (expanded) {
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
    order: FirebaseOrderData,
    onStatusClick: () -> Unit
) {

    SecondaryCard(
        modifier =
            Modifier.fillMaxWidth(),
        contentPadding =
            PaddingValues(16.dp)
    ) {

        TextNormal(
            text =
                "Order ID: ${order.orderId}",
            fontWeight =
                FontWeight.Bold,
            fontSize = 15.sp,
            maxLines = 1
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        TextNormal(
            text =
                formatOrderTime(
                    order.timestamp
                        .toDate()
                ),
            fontSize = 14.sp
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = onStatusClick,
            enabled =
                order.status.uppercase() !=
                        "COMPLETED"
        ) {

            Text(
                text =
                    order.status.uppercase()
            )
        }
    }
}

fun formatOrderTime(
    date: java.util.Date
): String {

    return java.text.SimpleDateFormat(
        "d MMM yyyy, h:mm a",
        java.util.Locale.getDefault()
    ).format(date)
}


@Composable
fun VendorFoodItem(
    product: FirebaseProductData,
    onClick: () -> Unit
) {

    SecondaryCard(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        contentPadding =
            PaddingValues(12.dp)
    ) {

        ProductImage(
            product = product,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )

        mySpacer()

        TextNormal(
            text =
                product.productName,
            fontWeight =
                FontWeight.Bold,
            fontSize = 20.sp
        )

        TextNormal(
            text =
                "RM %.2f".format(
                    product.productPrice
                ),
            fontSize = 16.sp
        )
    }
}

private fun productImageUrl(
    imageId: String
): String {

    return "https://curtineat-image-api.work-gordonyewyangliew.workers.dev/images/$imageId"
}

@Composable
fun ProductImage(
    product: FirebaseProductData,
    modifier: Modifier = Modifier
) {

    if (
        product.productImage.startsWith(
            "local:"
        )
    ) {

        val context =
            LocalContext.current

        val imageName =
            product.productImage
                .removePrefix("local:")

        val imageRes =
            context.resources.getIdentifier(
                imageName,
                "drawable",
                context.packageName
            )

        if (imageRes != 0) {

            Image(
                painter =
                    painterResource(imageRes),
                contentDescription =
                    product.productName,
                modifier = modifier,
                contentScale =
                    ContentScale.Crop
            )
        }

    } else {

        AsyncImage(
            model =
                productImageUrl(
                    product.productImage
                ),
            contentDescription =
                product.productName,
            modifier = modifier,
            contentScale =
                ContentScale.Crop
        )
    }
}