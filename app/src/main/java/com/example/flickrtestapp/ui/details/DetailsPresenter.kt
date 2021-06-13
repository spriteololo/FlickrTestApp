package com.example.flickrtestapp.ui.details

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.api.FlickrApi
import com.example.flickrtestapp.data.apimodel.BaseResponse
import com.example.flickrtestapp.data.apimodel.ErrorResponse
import com.example.flickrtestapp.data.apimodel.SizeResponse
import com.example.flickrtestapp.data.exceptions.FlickrApiException
import com.example.flickrtestapp.util.RxUtils
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable

@InjectViewState
class DetailsPresenter(
    private val flickrApi: FlickrApi,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val gson: Gson
) : MvpPresenter<DetailsView>() {

    private var detailsRequestDisposable: Disposable = Disposable.disposed()
    fun getSizes(id: String) {
        RxUtils.dispose(detailsRequestDisposable)
        detailsRequestDisposable = flickrApi.getSizes(photoId = id)
            .subscribeOn(ioScheduler)
            .map { responseBody ->
                val responseBodyStr = responseBody.string()
                val baseResponse = gson.fromJson(responseBodyStr, BaseResponse::class.java)
                if (baseResponse.stat == Constants.REQUEST_SUCCESS) {
                    gson.fromJson(responseBodyStr, SizeResponse::class.java)
                } else {
                    val errorResponse = gson.fromJson(responseBodyStr, ErrorResponse::class.java)
                    throw FlickrApiException(errorResponse)
                }
            }
            .map { sizeResponse -> sizeResponse.sizes.sizeList.maxByOrNull { it.width }!!.source }
            .observeOn(uiScheduler)
            .subscribe({ source ->
                viewState.updateImageUrl(source)
            }) { exception ->
                viewState.showError(exception)
            }
    }
}