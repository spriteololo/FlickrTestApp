package com.example.flickrtestapp.network.interceptors

import com.example.flickrtestapp.data.exceptions.NoInternetException
import com.example.flickrtestapp.network.NetworkChangeReceiver
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val networkChangeReceiver: NetworkChangeReceiver):
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoInternetException()
        }
        return chain.proceed(chain.request())
    }

    private fun isConnected(): Boolean {
        return networkChangeReceiver.connectivitySubject.value ?: false
    }
}