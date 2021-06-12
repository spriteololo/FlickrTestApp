package com.example.flickrtestapp.ui.view.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

internal class ProgressViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    companion object {
        fun inflate(@LayoutRes layoutResId: Int, parent: ViewGroup): ProgressViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return ProgressViewHolder(layoutInflater.inflate(layoutResId, parent, false))
        }
    }
}