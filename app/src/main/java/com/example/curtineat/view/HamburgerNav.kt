package com.example.curtineat.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.History
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
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onHistoryClick: () -> Unit,
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
                onProfileClick = {
                    closeDrawer()
                    onProfileClick()
                },
                onWalletClick = {
                    closeDrawer()
                    onWalletClick()
                },
                onHistoryClick = {
                    closeDrawer()
                    onHistoryClick()
                },
                onSettingClick = {
                    closeDrawer()
                },
                onLogInClick = {
                    closeDrawer()
                    onLoginClick()
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
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingClick: () -> Unit,
    onLogInClick: () -> Unit
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
            label = { Text("Profile") },
            icon = {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
            },
            selected = false,
            onClick = onProfileClick
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
    }
}