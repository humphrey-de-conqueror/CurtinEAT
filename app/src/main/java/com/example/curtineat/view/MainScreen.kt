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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.viewmodel.AppViewModel


@Composable
fun MainScreen(
    appViewModel: AppViewModel,
    onCardButtonClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    MainDrawer(
        onHomeClick = onHomeClick
    ) { onMenuClick ->

        Scaffold(
            topBar = {
                TopBarScreen(onMenuClick)
            },
            bottomBar = {
                BottomBarScreen()
            },
            floatingActionButton = {
                CardButton(onCardButtonClick)
            }
        ) { innerPadding ->
            BodyScreen(innerPadding)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScreen(
    onMenuClick: () -> Unit
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var showNotifications by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (showSearch) {
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
                Text("CurtinEAT")
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

            // Search / Close search
            IconButton(
                onClick = {
                    showSearch = !showSearch

                    if (!showSearch) {
                        searchText = ""
                    }
                }
            ) {
                Icon(
                    imageVector =
                        if (showSearch) Icons.Default.Close
                        else Icons.Default.Search,
                    contentDescription = "Search"
                )
            }



            Box {
                IconButton(
                    onClick = {
                        showNotifications = !showNotifications
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }

                DropdownMenu(
                    expanded = showNotifications,
                    onDismissRequest = {
                        showNotifications = false
                    },
                    modifier = Modifier
                        .widthIn(min = 280.dp, max = 320.dp)
                        .heightIn(min = 300.dp, max = 400.dp)
                ) {

                    // Header
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
                                showNotifications = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }

                    HorizontalDivider()

                    // Notifications
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
    )

}


@Composable
fun BottomBarScreen() {
    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf(
        "Home",
        "Restaurants",
        "Profile"
    )

    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Restaurant,
        Icons.Default.Person
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                },
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item
                    )
                },
                label = {
                    Text(text = item)
                }
            )
        }
    }
}

@Composable
fun CardButton(onCardButtonClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onCardButtonClick()
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}

@Composable
fun BodyScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
    RestaurantCard()}
}