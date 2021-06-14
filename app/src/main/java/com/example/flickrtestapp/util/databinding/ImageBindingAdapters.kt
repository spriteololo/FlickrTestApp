package com.example.flickrtestapp.util.databinding

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.BindingAdapter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.example.flickrtestapp.BuildConfig

object ImageBindingAdapters {
    private const val IMAGE_CHANGE_DURATION = 300

    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter(
        value = ["imageUrl", "imageError", "placeholder", "imageScaleType", "placeHolderScaleType", "urlCacheKey", "circleCrop", "ignoreCache", "showChangeAnimation", "setTransformation"],
        requireAll = false
    )
    fun loadImage(
        imageView: ImageView,
        url: String?,
        imageError: Drawable?,
        placeholder: Drawable?,
        imageScaleType: ImageView.ScaleType?,
        placeHolderScaleType: ImageView.ScaleType?,
        urlCacheKey: String?,
        circleCrop: Boolean,
        ignoreCache: Boolean,
        showChangeAnimation: Boolean,
        transformation: BitmapTransformation?
    ) {
        val context = imageView.context
        if (context is Activity && context.isDestroyed) {
            return
        }
        val builder: RequestBuilder<Drawable>
        val requestManager = Glide.with(context)
        builder = requestManager.load(url)

        if (!showChangeAnimation) {
            builder.transition(GenericTransitionOptions.with(android.R.anim.fade_in))
        }
        builder.placeholder(placeholder)
            .error(imageError)
        if (placeHolderScaleType != null) {
            imageView.scaleType = placeHolderScaleType
        }
        if (transformation != null) {
            builder.transform(transformation)
        }
        builder.listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>,
                isFirstResource: Boolean
            ): Boolean {
                if (BuildConfig.DEBUG) {
                    Log.w("GLIDE", "Glide load failed")
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                if (imageScaleType != null) {
                    imageView.scaleType = imageScaleType
                }
                if (showChangeAnimation) {
                    val imTarget = target as ImageViewTarget<*>
                    val animation = AlphaAnimation(0f, 1f)
                    animation.duration = IMAGE_CHANGE_DURATION.toLong()
                    imTarget.view.startAnimation(animation)
                }
                return false
            }
        })
        if (circleCrop) {
            builder.apply(RequestOptions.circleCropTransform())
        }
        builder.into(imageView)
    }

    @JvmStatic
    @BindingAdapter("imageRatio")
    fun setConstraintRatio(view: ImageView, ratio: String) {
        val constraintLayout = view.parent as ConstraintLayout
        with(ConstraintSet()) {
            clone(constraintLayout)
            setDimensionRatio(view.id, ratio)
            applyTo(constraintLayout)
        }
    }
}