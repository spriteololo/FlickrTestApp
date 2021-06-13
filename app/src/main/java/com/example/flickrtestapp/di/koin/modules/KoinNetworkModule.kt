package com.example.flickrtestapp.di.koin.modules

import android.content.Context
import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.BuildConfig
import com.example.flickrtestapp.network.NetworkChangeReceiver
import com.example.flickrtestapp.network.interceptors.NetworkConnectionInterceptor
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val koinNetworkModule = module {
    single {
        getOkHttpClient(
            get<Cache>(),
            get<StethoInterceptor>(),
            get<NetworkConnectionInterceptor>()
        )
    }
    single { getStethoInterceptor(androidContext()) }

    single { NetworkChangeReceiver(androidContext()) }
    single { NetworkConnectionInterceptor(get<NetworkChangeReceiver>()) }
}

private fun getOkHttpClient(
    cache: Cache,
    stethoInterceptor: StethoInterceptor,
    networkInterceptor: NetworkConnectionInterceptor
): OkHttpClient {
    val builder = newBaseOkHttpClient(cache)

    builder.addInterceptor(networkInterceptor)
    if (BuildConfig.DEBUG) {
        builder.addNetworkInterceptor(stethoInterceptor)
        builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }

    return builder.build()
}

private fun newBaseOkHttpClient(cache: Cache): OkHttpClient.Builder {
    return OkHttpClient.Builder()
        .connectTimeout(Constants.HTTP_CLIENT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(Constants.HTTP_CLIENT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(Constants.HTTP_CLIENT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
        .pingInterval(1, TimeUnit.SECONDS)
        .cache(cache)
}

private fun getStethoInterceptor(
    context: Context
): StethoInterceptor {
    Stetho.initialize(
        Stetho.newInitializerBuilder(context)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
            .build()
    )
    return StethoInterceptor()
}