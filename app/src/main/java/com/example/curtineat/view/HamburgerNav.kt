package com.example.curtineat.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun MainDrawer(
    //waiting for onHistoryClick and onSettingClick
    onHomeClick: () -> Unit,
    onWalletClick:() -> Unit,
    onLoginClick: () -> Unit,
    onSimulateVendorUpdateClick: () -> Unit = {}, //mimic vendor updating an order
    content: @Composable (onMenuClick: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    fun closeDrawer() {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HamburgerNav(
                onHomeClick = {
                    closeDrawer()
                    onHomeClick()
                },
                onWalletClick = {
                    closeDrawer()
                    onWalletClick()
                },
                onHistoryClick = { closeDrawer() },
                onSettingClick = { closeDrawer() },
                onLogInClick = {
                    closeDrawer()
                    onLoginClick()
                },
                onSimulateVendorUpdateClick = {
                    closeDrawer()
                    onSimulateVendorUpdateClick()
                }
            )
        }
    ) {
        content {
            scope.launch {
                drawerState.open()
            }
        }
    }
}


@Composable
fun HamburgerNav(
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingClick: () -> Unit,
    onLogInClick: () -> Unit,
    onSimulateVendorUpdateClick: () -> Unit = {}
) {
    ModalDrawerSheet {

        Text(
            text = "CurtinEAT",
            modifier = Modifier.padding(16.dp)
        )

        NavigationDrawerItem(
            label = { Text("Home") },
            icon = {
                Icon(Icons.Default.Home, contentDescription = null)
            },
            selected = false,
            onClick = onHomeClick
        )

        NavigationDrawerItem(
            label = { Text("Wallet") },
            icon = { Icon(Icons.Default.Wallet, contentDescription = null) },
            selected = false,
            onClick = onWalletClick
        )

        NavigationDrawerItem(
            label = { Text("History") },
            icon = {
                Icon(Icons.Default.History, contentDescription = null)
            },
            selected = false,
            onClick = onHistoryClick
        )

        NavigationDrawerItem(
            label = { Text("Settings") },
            icon = {
                Icon(Icons.Default.Settings, contentDescription = null)
            },
            selected = false,
            onClick = onSettingClick
        )

        NavigationDrawerItem(
            label = { Text("Log In") },
            icon = {
                Icon(Icons.Default.Login, contentDescription = null)
            },
            selected = false,
            onClick = onLogInClick
        )

        // mimic vvendor update
        NavigationDrawerItem(
            label = { Text("Simulate Vendor Update") },
            icon = {
                Icon(Icons.Default.Storefront, contentDescription = null)
            },
            selected = false,
            onClick = onSimulateVendorUpdateClick
        )
    }
}