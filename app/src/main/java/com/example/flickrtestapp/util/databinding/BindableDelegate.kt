package com.example.flickrtestapp.util.databinding

import androidx.databinding.BaseObservable
import kotlin.reflect.KProperty

class BindableDelegate<in R : BaseObservable, T : Any?>(
    private var value: T,
    private val bindingId: Int,
    private val onChange: (() -> Unit)?
) {
    operator fun getValue(receiver: R, property: KProperty<*>): T = value

    operator fun setValue(receiver: R, property: KProperty<*>, value: T) {
        if (this.value != value) {
            this.value = value
            receiver.notifyPropertyChanged(bindingId)
            onChange?.invoke()
        }
    }
}

fun <R : BaseObservable, T : Any?> bindable(value: T, bindingId: Int): BindableDelegate<R, T> =
    BindableDelegate(value, bindingId, null)

fun <R : BaseObservable, T : Any?> bindable(value: T, bindingId: Int, onChange: () -> Unit): BindableDelegate<R, T> =
    BindableDelegate(value, bindingId, onChange)

