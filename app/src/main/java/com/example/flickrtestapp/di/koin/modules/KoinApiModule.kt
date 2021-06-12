package com.example.flickrtestapp.di.koin.modules

import com.example.flickrtestapp.api.FlickrApi
import org.koin.dsl.module
import retrofit2.Retrofit

val koinApiModule = module {

    factory { createApiInstance<FlickrApi>(get()) }
}

inline fun <reified T> createApiInstance(retrofit: Retrofit): T = retrofit.create(T::class.java)