package com.example.flickrtestapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import com.example.flickrtestapp.R
import com.example.flickrtestapp.data.vo.PhotoVo
import com.example.flickrtestapp.ui.common.BaseViewHolder
import com.example.flickrtestapp.ui.common.ItemClickListener


class PhotoCardViewHolder private constructor(val photoCardBinding: PhotoCardViewBinding) :
    BaseViewHolder<PhotoVo>(photoCardBinding) {

    override fun setVo(vo: PhotoVo, listener: ItemClickListener<PhotoVo>) {
        photoCardBinding.listener = listener
        photoCardBinding.photoVo = vo
        photoCardBinding.executePendingBindings()
    }

    fun setVo(vo: PhotoVo, listener: ItemClickListener<PhotoVo>, animateFromBottom: Boolean) {
        setVo(vo, listener)

        startAnimation(animateFromBottom)
    }

    companion object {

        fun inflate(
            inflater: LayoutInflater,
            parent: ViewGroup?
        ): BaseViewHolder<PhotoVo> {
            val binding = DataBindingUtil.inflate<PhotoCardViewBinding>(
                inflater,
                R.layout.item_photo_card,
                parent,
                false
            )
            return PhotoCardViewHolder(binding)
        }
    }
}