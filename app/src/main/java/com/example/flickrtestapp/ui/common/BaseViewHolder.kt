package com.example.flickrtestapp.ui.common

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrtestapp.R

abstract class BaseViewHolder<T>(private val dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root) {

    abstract fun setVo(vo: T, listener: ItemClickListener<T>)

    fun clearAnimation() {
        dataBinding.root.clearAnimation()
    }

    open fun startAnimation(animateFromBottom: Boolean) {
        val animationRes =
            if (animateFromBottom) R.anim.item_animation_appear_from_bottom
            else R.anim.item_animation_appear_from_top
        val animation: Animation =
            AnimationUtils.loadAnimation(dataBinding.root.context, animationRes)
        dataBinding.root.startAnimation(animation)
    }
}