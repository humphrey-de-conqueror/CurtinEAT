package com.example.curtineat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel

@Composable
fun MainScreen(
    appViewModel: AppViewModel,
    onCardButtonClick: () -> Unit
) {
    Scaffold(
        topBar = { TopBarScreen() },
        bottomBar = { BottomBarScreen() },
        floatingActionButton = { CardButton(onCardButtonClick) }
    ) { innerPadding ->
        BodyScreen(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScreen() {
    TopAppBar(
        title = {
            Text(text = "CurtinEat")
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
    val restaurants = listOf(
        "Restaurant Placeholder 1",
        "Restaurant Placeholder 2",
        "Restaurant Placeholder 3",
        "Restaurant Placeholder 4"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Welcome to CurtinEat"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Choose a restaurant or meal below."
            )
        }

        items(restaurants) { restaurant ->
            RestaurantPlaceholderCard(
                name = restaurant
            )
        }
    }
}

@Composable
fun RestaurantPlaceholderCard(
    name: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for restaurant image
            Card(
                modifier = Modifier.size(64.dp)
            ) {
                // Image will go here later
            }

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            Column {
                Text(text = name)

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Placeholder description"
                )

                Text(
                    text = "RM XX.XX"
                )
            }
        }
    }
}