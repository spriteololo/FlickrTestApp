package com.example.flickrtestapp.util.databinding

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

open class BaseBindableObservable : BaseObservable() {

    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    @Synchronized
    override fun addOnPropertyChangedCallback(listener: Observable.OnPropertyChangedCallback) {
        if (this.mCallbacks == null) {
            this.mCallbacks = PropertyChangeRegistry()
        }

        this.mCallbacks!!.add(listener)
    }

    @Synchronized
    override fun removeOnPropertyChangedCallback(listener: Observable.OnPropertyChangedCallback) {
        if (this.mCallbacks != null) {
            this.mCallbacks!!.remove(listener)
        }
    }

    @Synchronized
    override fun notifyChange() {
        if (this.mCallbacks != null) {
            this.mCallbacks!!.notifyCallbacks(this, 0, null)
        }
    }

    override fun notifyPropertyChanged(fieldId: Int) {
        if (this.mCallbacks != null) {
            this.mCallbacks!!.notifyCallbacks(this, fieldId, null)
        }
    }
}