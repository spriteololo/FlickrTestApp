package com.example.flickrtestapp.ui.details

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.arellomobile.mvp.MvpAppCompatFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.flickrtestapp.R
import com.example.flickrtestapp.data.vo.PhotoVo
import com.example.flickrtestapp.ui.AppActivity


class DetailsFragment : MvpAppCompatFragment() {
    private lateinit var binding: DetailsFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
        sharedElementReturnTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        }
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.photoVo = requireArguments().getParcelable(PHOTO_VO_EXTRA)

        (activity as? AppActivity)?.showAppBarAndBackBtn(appBarVisibility = true, visible = true)
        ViewCompat.setTransitionName(binding.photo, binding.photoVo!!.id)
        ViewCompat.setTransitionName(binding.title, "t_" + binding.photoVo!!.id)
        Glide
            .with(binding.photo)
            .load(binding.photoVo?.getUrl())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(binding.photo)

    }

    @Keep
    companion object {
        private const val PHOTO_VO_EXTRA = "photo_vo_extra"
        fun createBundle(photoVo: PhotoVo): Bundle {
            return bundleOf(PHOTO_VO_EXTRA to photoVo)
        }
    }
}