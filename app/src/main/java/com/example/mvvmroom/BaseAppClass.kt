package com.example.mvvmroom

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mvvmroom.interfaces.NetworkStateListener
import com.example.mvvmroom.utils.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseAppClass : Application() {
    lateinit var networkMonitor : NetworkMonitor
    private var networkStateListener: NetworkStateListener? = null
    private lateinit var networkReceiver: BroadcastReceiver

    override fun onCreate() {
        super.onCreate()
        appContext = this
        singleton = this
        registerNetworkReceiver()
        networkMonitor = NetworkMonitor(this)
    }

    companion object {
        var appContext: Context? = null

        private var singleton: BaseAppClass? = null

        val instance: BaseAppClass?
            get() {
                if (singleton == null) {
                    singleton = BaseAppClass()
                }
                return singleton
            }
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(networkReceiver)
        networkMonitor.unregisterNetworkCallback()

    }

    fun setNetworkStateListener(listener: NetworkStateListener) {
        networkStateListener = listener
    }

    private fun registerNetworkReceiver() {
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == NetworkMonitor.NETWORK_STATE_CHANGE_ACTION) {
                    networkStateListener?.onNetworkStateChanged(
                        intent.getBooleanExtra(
                            NetworkMonitor.EXTRA_IS_CONNECTED,
                            false
                        )
                    )
                }
            }
        }


        LocalBroadcastManager.getInstance(appContext!!).registerReceiver(networkReceiver,
            IntentFilter(NetworkMonitor.NETWORK_STATE_CHANGE_ACTION)
        )
    }
}