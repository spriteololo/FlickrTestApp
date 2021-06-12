package com.example.flickrtestapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.data.vo.PhotoVo
import com.example.flickrtestapp.ui.common.BaseItemsAdapter
import com.example.flickrtestapp.ui.common.BaseViewHolder
import com.example.flickrtestapp.ui.common.ItemClickListener
import com.example.flickrtestapp.util.extensions.calculateDiffAndSendToAdapter


class PhotoAdapter internal constructor(
    items: MutableList<PhotoVo>,
    private val listener: ItemClickListener<PhotoVo>
) : BaseItemsAdapter<PhotoVo>(items) {

    var itemsGoUp = true
        set(value) {
            if (field != value) {
                field = value
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PhotoVo> {
        return PhotoCardViewHolder.inflate(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<PhotoVo>, position: Int) {
        (holder as PhotoCardViewHolder).setVo(items[position], listener, itemsGoUp)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<PhotoVo>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val o = payloads.first() as Bundle
            for (key in o.keySet()) {
                val result = o.getString(key) ?: Constants.EMPTY_STRING
                (holder as PhotoCardViewHolder).photoCardBinding.photoVo?.let { photoVo ->
                    when (key) {
                        KEY_SERVER -> photoVo.server = result
                        KEY_ID -> photoVo.id = result
                        KEY_SECRET -> photoVo.secret = result
                        KEY_BIGGEST_PREVIEW_SIZE -> photoVo.biggestPreviewSize = result
                        KEY_TITLE -> photoVo.title = result
                        else -> {
                        }
                    }
                }
            }
        }
    }

    fun setItems(items: List<PhotoVo>, clearItems: Boolean) {
        val oldList = this.items.toMutableList()
        if (clearItems) {
            this.items.clear()
        }

        this.items.addAll(items)

        if (clearItems) {
            notifyDataSetChanged()
        } else {
            PhotosDiffUtil(oldList, this.items).calculateDiffAndSendToAdapter(this)
        }
    }

    class PhotosDiffUtil(private val oldList: List<PhotoVo>, private val newList: List<PhotoVo>) :
        BaseDiffUtil<PhotoVo>(oldList, newList) {
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val newPhotoVo: PhotoVo = newList[newItemPosition]
            val oldPhotoVo: PhotoVo = oldList[oldItemPosition]
            val diffBundle = Bundle()
            if (newPhotoVo.server !== oldPhotoVo.server) {
                diffBundle.putString(KEY_SERVER, newPhotoVo.server)
            }
            if (newPhotoVo.id !== oldPhotoVo.id) {
                diffBundle.putString(KEY_ID, newPhotoVo.id)
            }
            if (newPhotoVo.secret !== oldPhotoVo.secret) {
                diffBundle.putString(KEY_SECRET, newPhotoVo.secret)
            }
            if (newPhotoVo.biggestPreviewSize !== oldPhotoVo.biggestPreviewSize) {
                diffBundle.putString(KEY_BIGGEST_PREVIEW_SIZE, newPhotoVo.biggestPreviewSize)
            }
            if (newPhotoVo.title !== oldPhotoVo.title) {
                diffBundle.putString(KEY_TITLE, newPhotoVo.title)
            }

            return if (diffBundle.size() == 0) null else diffBundle
        }

        override fun areItemsTheSame(oldItem: PhotoVo, newItem: PhotoVo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PhotoVo, newItem: PhotoVo) =
            oldItem.getUrl() == newItem.getUrl() && oldItem.title == newItem.title
    }

    @Keep
    companion object {
        private const val KEY_SERVER = "KEY_SERVER"
        private const val KEY_ID = "KEY_ID"
        private const val KEY_SECRET = "KEY_SECRET"
        private const val KEY_BIGGEST_PREVIEW_SIZE = "KEY_BIGGEST_PREVIEW_SIZE"
        private const val KEY_TITLE = "KEY_TITLE"
    }
}