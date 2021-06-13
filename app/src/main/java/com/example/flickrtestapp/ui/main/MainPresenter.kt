package com.example.flickrtestapp.ui.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.api.FlickrApi
import com.example.flickrtestapp.data.apimodel.BaseResponse
import com.example.flickrtestapp.data.apimodel.ErrorResponse
import com.example.flickrtestapp.data.apimodel.PhotoResponse
import com.example.flickrtestapp.data.exceptions.FlickrApiException
import com.example.flickrtestapp.data.mapper.TypeMapper
import com.example.flickrtestapp.data.vo.PhotoVo
import com.example.flickrtestapp.util.RxUtils
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable

@InjectViewState
class MainPresenter(
    private val flickrApi: FlickrApi,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val gson: Gson
) : MvpPresenter<MainView>() {

    private var photoRequestDisposable: Disposable = Disposable.disposed()

    fun getRecentPictures(page: Int) {
        if (photoRequestDisposable.isDisposed) {
            photoRequestDisposable = flickrApi.getRecentPhotos(page = page)
                .subscribeOn(ioScheduler)
                .map { responseBody ->
                    val responseBodyStr = responseBody.string()
                    val baseResponse = gson.fromJson(responseBodyStr, BaseResponse::class.java)
                    if (baseResponse.stat == Constants.REQUEST_SUCCESS) {
                        gson.fromJson(responseBodyStr, PhotoResponse::class.java)
                    } else {
                        val errorResponse =
                            gson.fromJson(responseBodyStr, ErrorResponse::class.java)
                        throw FlickrApiException(errorResponse)
                    }
                }
                .map { photoResponse ->
                    return@map photoResponse.photoSearchWrapper.let { searchResponse ->
                        val hasNextPage = searchResponse.page < searchResponse.pages
                        val clearItems = searchResponse.page == 1
                        Triple(
                            TypeMapper.convertAsList(searchResponse.photos, PhotoVo::class.java),
                            clearItems,
                            hasNextPage
                        )
                    }
                }
                .observeOn(uiScheduler)
                .subscribe({ (items, clearItems, hasNextPage) ->
                    viewState.updateRecent(items, clearItems, hasNextPage, 0)
                }) { exception ->
                    viewState.showError(exception)
                }
        }
    }

    fun searchPhotos(query: String, page: Int) {
        RxUtils.dispose(photoRequestDisposable)
        photoRequestDisposable = flickrApi.searchPhotos(query = query, page = page)
            .subscribeOn(ioScheduler)
            .map { responseBody ->
                val responseBodyStr = responseBody.string()
                val baseResponse = gson.fromJson(responseBodyStr, BaseResponse::class.java)
                if (baseResponse.stat == Constants.REQUEST_SUCCESS) {
                    gson.fromJson(responseBodyStr, PhotoResponse::class.java)
                } else {
                    val errorResponse = gson.fromJson(responseBodyStr, ErrorResponse::class.java)
                    throw FlickrApiException(errorResponse)
                }
            }
            .map { photoResponse ->
                return@map TypeMapper.convertAsList(
                    photoResponse.photoSearchWrapper.photos,
                    PhotoVo::class.java
                ) to photoResponse
            }
            .observeOn(uiScheduler)
            .subscribe({ (items, photoResponse) ->
                photoResponse.photoSearchWrapper.let { searchResponse ->
                    val hasNextPage = searchResponse.page < searchResponse.pages
                    val clearItems = searchResponse.page == 1
                    val resultCount = searchResponse.total
                    viewState.updateRecent(items, clearItems, hasNextPage, resultCount)
                }

            }) { exception ->
                viewState.showError(exception)
            }

    }
}
