package com.example.flickrtestapp.api

import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.Constants.RequestQueries
import com.example.flickrtestapp.data.apimodel.PhotoResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/rest/")
    fun getRecentPhotos(
        @Query(RequestQueries.METHOD) method: String = Constants.ApiMethods.RECENT,
        @Query(RequestQueries.API_KEY) apiKey: String = Constants.API_KEY,
        @Query(RequestQueries.FORMAT) format: String = Constants.DEFAULT_FORMAT,
        @Query(RequestQueries.NO_JSON_CALLBACK) noJsonCallback: Boolean = Constants.NO_JSON_CALLBACK,
        @Query(RequestQueries.PER_PAGE) perPage: Int = Constants.DEFAULT_PER_PAGE,
        @Query(RequestQueries.PAGE) page: Int,
        @Query(RequestQueries.EXTRAS) extras: String = Constants.REQUEST_EXTRAS
    ): Single<PhotoResponse>

    @GET("services/rest/")
    fun searchPhotos(
        @Query(RequestQueries.METHOD) method: String = Constants.ApiMethods.SEARCH,
        @Query(RequestQueries.API_KEY) apiKey: String = Constants.API_KEY,
        @Query(RequestQueries.FORMAT) format: String = Constants.DEFAULT_FORMAT,
        @Query(RequestQueries.NO_JSON_CALLBACK) noJsonCallback: Boolean = Constants.NO_JSON_CALLBACK,
        @Query(RequestQueries.QUERY_TEXT) query: String,
        @Query(RequestQueries.PAGE) page: Int,
        @Query(RequestQueries.PER_PAGE) perPage: Int = Constants.DEFAULT_PER_PAGE,
        @Query(RequestQueries.EXTRAS) extras: String = Constants.REQUEST_EXTRAS
    ): Single<PhotoResponse>
}