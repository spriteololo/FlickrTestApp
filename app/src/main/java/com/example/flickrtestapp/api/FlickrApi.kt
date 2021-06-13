package com.example.flickrtestapp.api

import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.Constants.RequestQueries
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FlickrApi {

    @GET(Constants.API_ENDPOINT)
    fun getRecentPhotos(
        @Query(RequestQueries.METHOD) method: String = Constants.ApiMethods.RECENT,
        @QueryMap map: Map<String, String> = Constants.QUERY_MAP_WITH_EXTRAS,
        @Query(RequestQueries.PAGE) page: Int,
        @Query(RequestQueries.PER_PAGE) perPage: Int = Constants.DEFAULT_PER_PAGE
    ): Single<ResponseBody>

    @GET(Constants.API_ENDPOINT)
    fun searchPhotos(
        @Query(RequestQueries.METHOD) method: String = Constants.ApiMethods.SEARCH,
        @QueryMap map: Map<String, String> = Constants.QUERY_MAP_WITH_EXTRAS,
        @Query(RequestQueries.QUERY_TEXT) query: String,
        @Query(RequestQueries.PAGE) page: Int,
        @Query(RequestQueries.PER_PAGE) perPage: Int = Constants.DEFAULT_PER_PAGE
    ): Single<ResponseBody>

    @GET(Constants.API_ENDPOINT)
    fun getSizes(
        @Query(RequestQueries.METHOD) method: String = Constants.ApiMethods.SIZES,
        @QueryMap map: Map<String, String> = Constants.DEFAULT_QUERY_MAP,
        @Query(RequestQueries.PHOTO_ID) photoId: String,
    ): Single<ResponseBody>
}