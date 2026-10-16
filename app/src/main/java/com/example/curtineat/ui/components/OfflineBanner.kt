package com.example.curtineat.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color

@Composable
fun OfflineBanner(
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    var wasOffline by remember { mutableStateOf(false) }
    var showBackOnline by remember { mutableStateOf(false) }

    LaunchedEffect(isOnline) {
        if (!isOnline) {
            wasOffline = true
            showBackOnline = false
        } else if (wasOffline) {
            showBackOnline = true
            delay(2000)
            showBackOnline = false
            wasOffline = false
        }
    }

    AnimatedVisibility(
        visible = !isOnline || showBackOnline,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut(),
        modifier = modifier
    ) {
        val offline = !isOnline
        val container = if (offline) MaterialTheme.colorScheme.errorContainer else Color(0xFF2E7D32)
        val content = if (offline) MaterialTheme.colorScheme.onErrorContainer else Color.White

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(container)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .semantics { liveRegion = LiveRegionMode.Polite },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (offline) Icons.Default.WifiOff else Icons.Default.Wifi,
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            AppText(
                text = if (offline) "No internet connection" else "Back online",
                style = MaterialTheme.typography.labelLarge,
                color = content,
                fontWeight = FontWeight.Medium
            )
        }
    }
}