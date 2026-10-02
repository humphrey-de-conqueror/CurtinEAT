package com.example.curtineat.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.database.Product
import androidx.compose.foundation.lazy.items
import com.example.daodao.Vendor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import com.example.curtineat.R
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.SecondaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer


@Composable
fun AppScaffold(
    // expect onHistoryClick and onSettingClick
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLogInClick: () -> Unit = {},
    onVendorToggle: (Boolean) -> Unit = {},
    title: String = "CurtinEAT",
    showSearch: Boolean = true,
    showNotifications: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    MainDrawer(
        // expect to give onHistoryClick and onSettingClick
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick
        onHomeClick = onHomeClick,
        onLogInClick = onLogInClick,
        isVendorMode = appViewModel.isVendorMode,
        onVendorToggle = onVendorToggle
    ) { onMenuClick ->

        Scaffold(
            topBar = {
                TopBarScreen(
                    appViewModel = appViewModel,
                    onMenuClick = onMenuClick,
                    title = title,
                    showSearch = showSearch,
                    showNotifications = showNotifications
                )
            },
            floatingActionButton = floatingActionButton
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

@Composable
fun MainScreen(
    //expect onHistory and onSetting
    appViewModel: AppViewModel,
    onCartButtonClick: () -> Unit,
    onHomeClick: () -> Unit,
    onWalletClick:() -> Unit,
    onLoginClick: () -> Unit
    onHomeClick: () -> Unit,
    onVendorToggle: (Boolean) -> Unit = {}
) {
    AppScaffold(
        // expect onHistory and onSetting
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        onVendorToggle = onVendorToggle,
        title = "CurtinEAT",
        showSearch = true,
        showNotifications = true,
        floatingActionButton = {
            CartButton(
                onCartButtonClick = onCartButtonClick,
                totalQuantity = appViewModel.cart.sumOf { it.quantity }
            )
        }
    ) { innerPadding ->

        BodyScreen(
            innerPadding = innerPadding,
            appViewModel = appViewModel
            // removed
//            vendors = appViewModel.vendor,
//            products = appViewModel.product
        )
    }
}

@Composable
fun NotificationItem(
    message: String,
    time: String,
    onClick: () -> Unit = {}
) {
    SecondaryCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = 16.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextNormal(
                text = message,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp
            )

            TextNormal(
                text = time,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScreen(
    appViewModel: AppViewModel,
    onMenuClick: () -> Unit,
    title: String = "CurtinEAT",
    showSearch: Boolean = true,
    showNotifications: Boolean = true
) {
    var searching by remember { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var notificationsOpen by remember { mutableStateOf(false) }

    val notificationSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false //allows partial to expanded behavior
    )

    TopAppBar(
        title = {
            if (searching && showSearch) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        TextNormal(
                            text = "Search food...",
                            fontSize = 16.sp
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),

                    // change the meaning of phone's bottom right ENTER key
                    keyboardOptions = KeyboardOptions( imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            appViewModel.searchProduct(productName = searchText)
                        }
                    )
                )
            } else {
                TextNormal(
                    text = "CurtinEAT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },

        navigationIcon = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },

        actions = {

            // SEARCH
            if (showSearch) {
                IconButton(
                    onClick = {
                        searching = !searching

                        if (!searching) {
                            searchText = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector =
                            if (searching)
                                Icons.Default.Close
                            else
                                Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }

            // NOTIFICATION
            if (showNotifications) {
                IconButton(
                    onClick = {
                        notificationsOpen = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }
            //login goes here


        }
    )
    if (notificationsOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                notificationsOpen = false
            },
            sheetState = notificationSheetState
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f) //swipe up to 85% screen

            ) {
                items(
                    items = appViewModel.notification,
                    key = { eachNotification -> eachNotification.notificationId}
                ) { eachNotification ->
                    Text(text = eachNotification.message ?: "dump dump empty message")
                }
            }
        }
    }
}


@Composable
fun CartButton(
    onCartButtonClick: () -> Unit,
    totalQuantity: Int
) {
    Box {

        FloatingActionButton(
            onClick = onCartButtonClick
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart"
            )
        }

        if (totalQuantity > 0) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = totalQuantity.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BodyScreen(
    innerPadding: PaddingValues,
    appViewModel: AppViewModel
) {
    val listState = rememberLazyListState()

    LaunchedEffect(
        appViewModel.searchVendorId,
        appViewModel.vendor
    ) {
        val searchVendorId = appViewModel.searchVendorId

        if (searchVendorId != null) {
            val index = appViewModel.vendor.indexOfFirst {
                it.vendorId == searchVendorId
            }

            println("VENDOR INDEX = $index")

            if (index >= 0) {
                listState.animateScrollToItem(index)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(innerPadding)
    ) {
        items(
            items = appViewModel.vendor,
            key = { eachVendor -> eachVendor.vendorId }
        ) { eachVendor ->

            val vendorProducts = appViewModel.product.filter {
                it.vendorID == eachVendor.vendorId
            }
            RestaurantCard(appViewModel = appViewModel, eachVendor, vendorProducts)
        }
    }
}

@Composable
fun RestaurantCard(
    appViewModel: AppViewModel,
    vendor: Vendor,
    products: List<Product>
) {
    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        TextNormal(
            text = vendor.vendorName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        mySpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextNormal(
                    text = "%.1f".format(vendor.rating),
                    fontSize = 18.sp
                )

                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xffEA7422),
                    modifier = Modifier.size(20.dp)
                )
            }

            TextNormal(
                text = vendor.category
            )

            TextNormal(
                text = "%.1f km".format(vendor.distance)
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                FoodItem(
                    appViewModel = appViewModel,
                    product = product
                )
            }
        }
    }
}


//Helper to get product Image
@Composable
fun getDrawableId(imageName: String): Int {
    return try {
        R.drawable::class.java
            .getField(imageName)
            .getInt(null)
    } catch (e: Exception) {
        R.drawable.food1
    }
}

@Composable
fun FoodItem(
    appViewModel: AppViewModel,
    product: Product
) {
    val imageRes = getDrawableId(product.productImage)

    SecondaryCard(
        onClick = {
            appViewModel.addToCart(product)
        },

        modifier = Modifier.width(150.dp)
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = product.productName,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            TextNormal(
                text = "RM %.2f".format(product.productPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            TextNormal(
                text = product.productName,
                fontSize = 14.sp
            )
        }
    }
}