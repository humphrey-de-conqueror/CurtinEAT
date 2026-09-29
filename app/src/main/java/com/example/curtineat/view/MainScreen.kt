package com.example.curtineat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.database.Product


@Composable
fun AppScaffold(
    onHomeClick: () -> Unit,
    title: String = "CurtinEAT",
    showSearch: Boolean = true,
    showNotifications: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    MainDrawer(
        onHomeClick = onHomeClick
    ) { onMenuClick ->

        Scaffold(
            topBar = {
                TopBarScreen(
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
    onHomeClick: () -> Unit
) {
    AppScaffold(
        onHomeClick = onHomeClick,
        title = "CurtinEAT",
        showSearch = true,
        showNotifications = true,
        floatingActionButton = {
            CartButton(onCartButtonClick)
        }
    ) { innerPadding ->

        BodyScreen(
            innerPadding = innerPadding,
            products = appViewModel.product
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScreen(
    onMenuClick: () -> Unit,
    title: String = "CurtinEAT",
    showSearch: Boolean = true,
    showNotifications: Boolean = true
) {
    var searching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // controls dropdown open/close
    var notificationsOpen by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (searching && showSearch) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text("Search food...")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(title)
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

                Box {
                    IconButton(
                        onClick = {
                            notificationsOpen = !notificationsOpen
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }

                    DropdownMenu(
                        expanded = notificationsOpen,
                        onDismissRequest = {
                            notificationsOpen = false
                        },
                        modifier = Modifier
                            .widthIn(min = 280.dp, max = 320.dp)
                            .heightIn(min = 300.dp, max = 400.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Notifications",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            IconButton(
                                onClick = {
                                    notificationsOpen = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }

                        HorizontalDivider()

                        repeat(10) { index ->
                            Text(
                                text = "Notification ${index + 1}",
                                modifier = Modifier.padding(16.dp)
                            )

                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    )
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
    products: List<Product>
) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        RestaurantCard(products = products)
    }
}