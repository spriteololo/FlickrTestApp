package com.example.flickrtestapp.ui.common

import android.view.View

interface ItemClickListener<T> {
    fun onItemClick(view: View, item: T)
}