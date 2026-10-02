package com.example.curtineat.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip


@Composable
fun MainDrawer(
    //waiting for onHistoryClick and onSettingClick
    onHomeClick: () -> Unit,
    onWalletClick:() -> Unit,
    onLoginClick: () -> Unit,
    onLogInClick: () -> Unit = {},
    isVendorMode: Boolean = false,
    onVendorToggle: (Boolean) -> Unit = {},
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
                }
                onLogInClick = {
                    closeDrawer()
                    onLogInClick()
                },
                isVendorMode = isVendorMode,
                onVendorToggle = {
                    closeDrawer()
                    onVendorToggle(it)
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
fun ToggleBox(
    isVendorMode: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val boxColor by animateColorAsState(
        targetValue = if (isVendorMode)
            MaterialTheme.colorScheme.tertiary
        else
            MaterialTheme.colorScheme.primary,
        label = "modeToggleColor"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(boxColor)
            .clickable { onToggle(!isVendorMode) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isVendorMode) "Vendor Mode" else "Customer Mode",
            color = if (isVendorMode)
                MaterialTheme.colorScheme.onTertiary
            else
                MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun HamburgerNav(
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingClick: () -> Unit,
    onLogInClick: () -> Unit,
    isVendorMode: Boolean,
    onVendorToggle: (Boolean) -> Unit
) {
    ModalDrawerSheet {

        Text(
            text = "CurtinEAT",
            modifier = Modifier.padding(16.dp)
        )

        ToggleBox(
            isVendorMode = isVendorMode,
            onToggle = onVendorToggle
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
    }
}