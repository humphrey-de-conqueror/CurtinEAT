package com.example.curtineat.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    TopAppBar(
        title = {
            Text("CurtinEAT")
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