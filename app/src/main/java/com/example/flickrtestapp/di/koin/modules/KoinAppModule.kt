package com.example.flickrtestapp.di.koin.modules

import com.example.flickrtestapp.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val koinAppModule = module {

    single { Cache(androidApplication().cacheDir, Constants.HTTP_DISK_CACHE_SIZE) }
    single { getGson() }
}


private fun getGson(): Gson {
    return GsonBuilder().create()
}