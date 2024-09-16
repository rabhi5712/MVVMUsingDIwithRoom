package com.example.mvvmroom.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mvvmroom.BaseAppClass

class NetworkMonitor(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var state = false
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    println("NetworkMonitor Network Available")
                    state = true
                    sendBroadcast(true, context)
                }

                override fun onLost(network: Network) {
                    println("NetworkMonitor Network Lost")
                    state = false
                    sendBroadcast(false, context)
                }
            })
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            connectivityManager.registerNetworkCallback(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        println("NetworkMonitor Network Available")
                        state = true
                        sendBroadcast(true, context)
                    }

                    override fun onLost(network: Network) {
                        println("NetworkMonitor Network Lost")
                        state = false
                        sendBroadcast(false, context)
                    }
                })
        }
    }

    private fun sendBroadcast(isConnected: Boolean, context: Context) {
        val intent = Intent(NETWORK_STATE_CHANGE_ACTION)
        intent.putExtra(EXTRA_IS_CONNECTED, isConnected)
        val localBroadcastManager =
            LocalBroadcastManager.getInstance(BaseAppClass.appContext!!)
        localBroadcastManager.sendBroadcast(intent)
    }

    fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {})
    }

    companion object {
        const val NETWORK_STATE_CHANGE_ACTION = "com.example.mvvmroom.NetworkStateChange"
        const val EXTRA_IS_CONNECTED = "isConnected"
    }
}
