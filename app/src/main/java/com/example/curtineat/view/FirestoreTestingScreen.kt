package com.example.curtineat.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
import com.example.curtineat.viewmodel.AppViewModel

@Composable
fun FirestoreTestingScreen(
    appViewModel: AppViewModel
) {
    val products by appViewModel.products.collectAsState()

    LaunchedEffect(Unit) {
        appViewModel.loadProducts()
    }

    if (products.isEmpty()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()

            mySpacer()

            TextNormal(
                text = "Loading products..."
            )
        }

    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            item {

                TextNormal(
                    text = "Firestore Products",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )

                mySpacer()

                PrimaryButton(
                    text = "Does nothing",
                    onClick = {
                        // Testing button
                    }
                )
            }

            items(
                items = products,
                key = { it.productId }
            ) { product ->

                val imageRes = getDrawableId(product.productImage)

                PrimaryCard(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = product.productName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentScale = ContentScale.Crop
                    )

                    mySpacer()

                    TextNormal(
                        text = product.productName,
                        fontWeight = FontWeight.Bold
                    )

                    TextNormal(
                        text = "Product ID: ${product.productId}",
                        fontSize = 14.sp
                    )

                    TextNormal(
                        text = "Vendor ID: ${product.vendorId}",
                        fontSize = 14.sp
                    )

                    TextNormal(
                        text = "RM %.2f".format(product.productPrice),
                        fontSize = 16.sp
                    )
                }

                mySpacer()
            }
        }
    }
}