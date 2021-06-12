package com.example.flickrtestapp.util.extensions

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

fun DiffUtil.Callback.calculateDiffAndSendToAdapter(
    adapter: RecyclerView.Adapter<*>,
    detectMoves: Boolean = true
) {
    /*Single.fromCallable { DiffUtil.calculateDiff(this, detectMoves) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            result.dispatchUpdatesTo(adapter)
        }*/
    DiffUtil.calculateDiff(this, detectMoves).dispatchUpdatesTo(adapter)
}