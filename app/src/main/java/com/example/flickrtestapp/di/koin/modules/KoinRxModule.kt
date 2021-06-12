package com.example.flickrtestapp.di.koin.modules

import android.os.Looper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SCHEDULER_UI = "observe"
const val SCHEDULER_IO = "io"
const val SCHEDULER_COMPUTATION = "computation"

val koinRxModule = module {
    single(named(SCHEDULER_UI)) { AndroidSchedulers.from(Looper.getMainLooper(), true) }
    single(named(SCHEDULER_IO)) { Schedulers.io() }
    single(named(SCHEDULER_COMPUTATION)) { Schedulers.computation() }
}