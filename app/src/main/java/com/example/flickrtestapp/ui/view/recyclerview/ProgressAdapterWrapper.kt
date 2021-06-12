package com.example.flickrtestapp.ui.view.recyclerview

import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.flickrtestapp.R

class ProgressAdapterWrapper(
    private val endlessRecyclerView: EndlessRecyclerView,
    val innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    private var isProgressEnable: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var observer: AdapterDataObserver? = null
    private val progressPosition: Int
        get() = if (isProgressEnable) itemCount - 1 else -1

    fun reset() {
        innerAdapter.unregisterAdapterDataObserver(observer!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType != R.id.erv_progress) {
            innerAdapter.onCreateViewHolder(parent, viewType)
        } else {
            ProgressViewHolder.inflate(endlessRecyclerView.progressLayoutId, parent)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (!isProgressEnable || position != progressPosition) {
            innerAdapter.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isProgressEnable) {
            if (position == progressPosition) R.id.erv_progress else innerAdapter.getItemViewType(
                position
            )
        } else {
            innerAdapter.getItemViewType(position)
        }
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
        innerAdapter.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return if (isProgressEnable) {
            if (position == progressPosition) {
                R.id.erv_progress.toLong()
            } else {
                val itemId = innerAdapter.getItemId(position)
                if (itemId == R.id.erv_progress.toLong()) {
                    throw RuntimeException("Item has same id that endless scroll progress.")
                }
                itemId
            }
        } else {
            innerAdapter.getItemId(position)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is ProgressViewHolder) {
            super.onViewRecycled(holder)
        } else {
            innerAdapter.onViewRecycled(holder)
        }
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return if (holder.adapterPosition != progressPosition) {
            innerAdapter.onFailedToRecycleView(holder)
        } else {
            super.onFailedToRecycleView(holder)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder.adapterPosition != progressPosition) {
            innerAdapter.onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (holder.adapterPosition != progressPosition) {
            innerAdapter.onViewAttachedToWindow(holder)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        if (observer == null) {
            observer = InnerAdapterDataObserverWrapper()
            innerAdapter.registerAdapterDataObserver(observer as InnerAdapterDataObserverWrapper)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        innerAdapter.onDetachedFromRecyclerView(recyclerView)
        if (observer != null) {
            innerAdapter.unregisterAdapterDataObserver(observer!!)
            observer = null
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != progressPosition) {
            innerAdapter.onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return if (isProgressEnable) innerAdapter.itemCount + 1 else innerAdapter.itemCount
    }

    fun setProgressEnable(progressEnable: Boolean) {
        if (isProgressEnable != progressEnable) {
            if (progressEnable) {
                val progressPosition = innerAdapter.itemCount
                isProgressEnable = true
                notifyItemInserted(progressPosition)
            } else {
                val lastProgressPosition = progressPosition
                isProgressEnable = false
                notifyItemRemoved(lastProgressPosition)
            }
        }
    }

    private inner class InnerAdapterDataObserverWrapper : AdapterDataObserver() {
        override fun onChanged() {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(
            @IntRange(from = 0) positionStart: Int,
            @IntRange(from = 1) itemCount: Int
        ) {
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(
            @IntRange(from = 0) positionStart: Int,
            @IntRange(from = 1) itemCount: Int,
            payload: Any?
        ) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(
            @IntRange(from = 0) positionStart: Int,
            @IntRange(from = 1) itemCount: Int
        ) {
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(
            @IntRange(from = 0) positionStart: Int,
            @IntRange(from = 1) itemCount: Int
        ) {
            notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(
            @IntRange(from = 0) fromPosition: Int,
            @IntRange(from = 0) toPosition: Int,
            @IntRange(from = 1) itemCount: Int
        ) {
            if (itemCount == 1) {
                notifyItemMoved(fromPosition, toPosition)
            } else {
                var from = fromPosition
                var to = toPosition
                val end = toPosition + itemCount
                while (from < end) {
                    notifyItemMoved(from, to)
                    from++
                    to++
                }
            }
        }
    }
}