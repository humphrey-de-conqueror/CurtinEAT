package com.example.curtineat.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class OnlineCheck(context: Context) {

    private val cm = context.getSystemService(ConnectivityManager::class.java)

    private fun online(caps: NetworkCapabilities?) =
        caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true

    val isOnline: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(n: Network, caps: NetworkCapabilities) {
                trySend(online(caps))
            }

            override fun onLost(n: Network) {
                trySend(false)
            }
        }
        cm.registerDefaultNetworkCallback(callback)
        trySend(online(cm.getNetworkCapabilities(cm.activeNetwork)))
        awaitClose { cm.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}