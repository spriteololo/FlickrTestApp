package com.example.flickrtestapp.di.koin.modules

import com.example.flickrtestapp.api.FlickrApi
import com.example.flickrtestapp.ui.main.MainPresenter
import io.reactivex.rxjava3.core.Scheduler
import org.koin.core.qualifier.named
import org.koin.dsl.module

val koinPresenterModule = module {
    single {
        MainPresenter(
            get<FlickrApi>(),
            get<Scheduler>(named(SCHEDULER_IO)),
            get<Scheduler>(named(SCHEDULER_UI))
        )
    }
}