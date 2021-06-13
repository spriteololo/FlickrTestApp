package com.example.flickrtestapp.network

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}