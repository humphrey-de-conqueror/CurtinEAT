package com.example.curtineat.view

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
import com.example.curtineat.viewmodel.FirestoreState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.ui.res.painterResource



@Composable
fun FirestoreTestingScreen(
    appViewModel: AppViewModel
) {

    LaunchedEffect(Unit) {
        appViewModel.loadFirestoreProducts()
    }

    when (
        val state = appViewModel.firestoreState
    ) {

        FirestoreState.Idle -> {
        }


        FirestoreState.Loading -> {

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
        }


        FirestoreState.Empty -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextNormal(
                    text = "No products found"
                )

                mySpacer()

                PrimaryButton(
                    text = "Retry",
                    onClick = {
                        appViewModel.loadFirestoreProducts()
                    }
                )
            }
        }


        is FirestoreState.Error -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextNormal(
                    text = "Unable to load products",
                    fontWeight = FontWeight.Bold
                )

                TextNormal(
                    text = state.message,
                    fontSize = 14.sp
                )

                mySpacer()

                PrimaryButton(
                    text = "Retry",
                    onClick = {
                        appViewModel.loadFirestoreProducts()
                    }
                )
            }
        }


        is FirestoreState.Success -> {

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
                        text = "Seed Firestore",
                        onClick = {
                            appViewModel.seedFirestore()
                        }
                    )
                }


                items(
                    items = state.products,
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
                            text = "Vendor ID: ${product.vendorID}",
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
}