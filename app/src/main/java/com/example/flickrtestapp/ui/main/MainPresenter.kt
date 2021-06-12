package com.example.flickrtestapp.ui.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.flickrtestapp.api.FlickrApi
import com.example.flickrtestapp.data.mapper.TypeMapper
import com.example.flickrtestapp.data.vo.PhotoVo
import com.example.flickrtestapp.util.RxUtils
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable

@InjectViewState
class MainPresenter(
    private val flickrApi: FlickrApi,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
) : MvpPresenter<MainView>() {

    private var photoRequestDisposable: Disposable = Disposable.disposed()

    fun getRecentPictures(page: Int) {
        if (photoRequestDisposable.isDisposed) {
            photoRequestDisposable = flickrApi.getRecentPhotos(page = page)
                .subscribeOn(ioScheduler)
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
                    viewState.updateRecent(items, clearItems, hasNextPage)
                }) { TODO() }
        }
    }

    fun searchPhotos(query: String, page: Int) {
        RxUtils.dispose(photoRequestDisposable)
        photoRequestDisposable = flickrApi.searchPhotos(query = query, page = page)
            .subscribeOn(ioScheduler)
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
                viewState.updateRecent(items, clearItems, hasNextPage)
            }) { TODO() }

    }
}
