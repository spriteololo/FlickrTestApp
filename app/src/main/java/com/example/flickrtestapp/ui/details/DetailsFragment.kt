package com.example.flickrtestapp.ui.details

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Keep
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.transition.TransitionInflater
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.flickrtestapp.R
import com.example.flickrtestapp.data.exceptions.FlickrApiException
import com.example.flickrtestapp.data.exceptions.NoInternetException
import com.example.flickrtestapp.data.vo.PhotoVo
import com.example.flickrtestapp.ui.AppActivity
import com.example.flickrtestapp.ui.common.BaseInstanceSaverFragment
import org.koin.android.ext.android.get


class DetailsFragment : BaseInstanceSaverFragment(), DetailsView {
    private lateinit var binding: DetailsFragmentBinding
    private lateinit var loadedImageUrl: String

    @InjectPresenter
    lateinit var presenter: DetailsPresenter

    @ProvidePresenter
    fun providePresenter(): DetailsPresenter = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getString(LOADED_URL_EXTRA)?.let { url ->
            loadedImageUrl = url
        }
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
            .load(if(::loadedImageUrl.isInitialized) loadedImageUrl else binding.photoVo?.getUrl())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    if(!::loadedImageUrl.isInitialized) {
                        startPostponedEnterTransition()
                        presenter.getSizes(binding.photoVo!!.id)
                    }
                    binding.photo.setImageDrawable(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    if(!::loadedImageUrl.isInitialized) {
                        startPostponedEnterTransition()
                        presenter.getSizes(binding.photoVo!!.id)
                    }
                    super.onLoadFailed(errorDrawable)
                }
            })
    }

    override fun onNetworkStateChanged(networkAvailable: Boolean) {
        if (networkAvailable && !::loadedImageUrl.isInitialized) {
            binding.photoVo?.id?.let { presenter.getSizes(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::loadedImageUrl.isInitialized) {
            outState.putString(LOADED_URL_EXTRA, loadedImageUrl)
        }
        super.onSaveInstanceState(outState)
    }

    @Keep
    companion object {
        private const val LOADED_URL_EXTRA = "loaded_url_extra"
        private const val PHOTO_VO_EXTRA = "photo_vo_extra"
        fun createBundle(photoVo: PhotoVo): Bundle {
            return bundleOf(PHOTO_VO_EXTRA to photoVo)
        }
    }

    override fun updateImageUrl(imageUrl: String) {
        loadedImageUrl = imageUrl
        Glide.with(binding.photo)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .override(binding.photo.width, binding.photo.height)
            .into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    binding.photo.setImageDrawable(resource)
                }
            })

    }

    override fun showError(exception: Throwable) {
        when (exception) {
            is FlickrApiException -> Toast.makeText(context, exception.message, Toast.LENGTH_SHORT)
                .show()
            is NoInternetException -> {}
            else -> Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT)
                .show()
        }
    }
}