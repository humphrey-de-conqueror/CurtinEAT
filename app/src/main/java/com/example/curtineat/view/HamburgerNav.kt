package com.example.curtineat.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.History
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
                onHistoryClick = { closeDrawer() },
                onSettingClick = { closeDrawer() },
                onLogInClick = { closeDrawer() }
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