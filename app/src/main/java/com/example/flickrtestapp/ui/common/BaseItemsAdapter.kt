package com.example.flickrtestapp.ui.common

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseItemsAdapter<T>(protected val items: MutableList<T>) :
    RecyclerView.Adapter<BaseViewHolder<T>>() {

    override fun getItemCount() = items.size

    abstract override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int)

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<T>) {
        holder.clearAnimation()
    }

    abstract class BaseDiffUtil<T>(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return areItemsTheSame(oldItem, newItem)
        }

        abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return areContentsTheSame(oldItem, newItem)
        }

        abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    }
}