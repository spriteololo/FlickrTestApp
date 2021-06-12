package com.example.flickrtestapp.util

import io.reactivex.rxjava3.disposables.Disposable

object RxUtils {
    @JvmStatic
    fun dispose(disposable: Disposable?) {
        if (disposable == null) {
            return
        }
        if (disposable.isDisposed) {
            return
        }
        disposable.dispose()
    }

    @JvmStatic
    fun isDisposed(disposable: Disposable?): Boolean {
        return disposable == null || disposable.isDisposed
    }
}