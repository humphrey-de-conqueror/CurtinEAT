package com.example.curtineat.view

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
import com.example.curtineat.ui.theme.SecondaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer


@Composable
fun AppScaffold(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onLogInClick: () -> Unit = {},
    onVendorToggle: (Boolean) -> Unit = {},
    title: String = "CurtinEAT",
    showSearch: Boolean = true,
    showNotifications: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    MainDrawer(
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
    appViewModel: AppViewModel,
    onCartButtonClick: () -> Unit,
    onHomeClick: () -> Unit,
    onVendorToggle: (Boolean) -> Unit = {}
) {
    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onVendorToggle = onVendorToggle,
        title = "CurtinEAT",
        showSearch = true,
        showNotifications = true,
        floatingActionButton = {
            CartButton(onCartButtonClick)
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
    var searchText by remember { mutableStateOf("") }
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
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                TextNormal(
                    text = title,
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
fun CartButton(onCartButtonClick: () -> Unit) {
    FloatingActionButton(
        onClick = onCartButtonClick
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart"
        )
    }
}

@Composable
fun BodyScreen(
    innerPadding: PaddingValues,
    appViewModel: AppViewModel
) {
    LazyColumn(
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