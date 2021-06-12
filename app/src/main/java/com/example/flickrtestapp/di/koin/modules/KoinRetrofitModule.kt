package com.example.flickrtestapp.di.koin.modules

import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.BuildConfig
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val koinRetrofitModule = module {
    single { newRetrofit(get(), get()) }
    factory { GsonConverterFactory.create(get<Gson>()) }
}

private fun newRetrofit(
    client: OkHttpClient,
    factory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .validateEagerly(BuildConfig.DEBUG)
        .addConverterFactory(factory)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}