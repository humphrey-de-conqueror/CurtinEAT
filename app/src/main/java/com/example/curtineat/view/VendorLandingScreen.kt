package com.example.curtineat.view

import androidx.compose.foundation.Image
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.database.Product
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
import com.example.curtineat.viewmodel.AppViewModel

@Composable
fun VendorLandingScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onOrderStatusClick: () -> Unit,
    onFoodClick: (Int) -> Unit,
    onAddFoodClick: () -> Unit
) {
    val loggedInVendorId: Int? = 1
    // val loggedInVendorId = appViewModel.account.vendorId

    val currentVendor = appViewModel.vendor.find {
        it.vendorId == loggedInVendorId
    }

    val vendorProducts = appViewModel.product.filter {
        it.vendorID == loggedInVendorId
    }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        showSearch = true,
        showNotifications = true,

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFoodClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add food"
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

                mySpacer()

                PrimaryButton(
                    text = "Order Status",
                    onClick = onOrderStatusClick,
                    modifier = Modifier.fillMaxWidth()
                )

                mySpacer()

                TextNormal(
                    text = "My Product(s)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }

            items(
                items = vendorProducts,
                key = { it.productId }
            ) { product ->

                VendorFoodItem(
                    product = product,
                    onClick = {
                        onFoodClick(product.productId)
                    }
                )
            }
        }
    }
}

@Composable
fun VendorFoodItem(
    product: Product,
    onClick: () -> Unit
) {
    val imageRes = getDrawableId(product.productImage)

    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentPadding = PaddingValues(12.dp)
    ) {

        Image(
            painter = painterResource(imageRes),
            contentDescription = product.productName,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentScale = ContentScale.Crop
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