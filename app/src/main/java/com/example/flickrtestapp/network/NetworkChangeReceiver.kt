package com.example.flickrtestapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.reactivex.rxjava3.subjects.BehaviorSubject

class NetworkChangeReceiver(private val context: Context) {

    var connectivitySubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    private val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        var lastAvailableNetworkHash: Int = 0
        override fun onAvailable(network: Network) {
            if (network.hashCode() != lastAvailableNetworkHash) {
                lastAvailableNetworkHash = network.hashCode()
                connectivitySubject.onNext(true)
            }
        }

        override fun onLost(network: Network) {
            if (network.hashCode() == lastAvailableNetworkHash) {
                connectivitySubject.onNext(false)
            }
        }
    }

    init {
        registerReceiver()
    }

    /***
     * Register callback on wifi connectivity changes
     * @throws IllegalStateException if didn't subscribe to emitter firstly
     */
    private fun registerReceiver() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()
        NetworkUtils.getConnectivityManager(context).let { connManager ->
            connManager.registerNetworkCallback(networkRequest, connectivityCallback)
            connManager.activeNetwork?.let { connectivityCallback.onAvailable(it) }
                ?: connectivitySubject.onNext(false)
        }
    }

    /***
     * Unregister callback on wifi connectivity changes
     */
    @Suppress("unused")
    fun unregisterReceiver() {
        NetworkUtils.getConnectivityManager(context).unregisterNetworkCallback(connectivityCallback)
        connectivityCallback.lastAvailableNetworkHash = 0
    }
}