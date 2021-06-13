package com.example.flickrtestapp

import androidx.annotation.Keep

@Keep
object Constants {
    object ApiMethods {
        const val SEARCH = "flickr.photos.search"
        const val RECENT = "flickr.photos.getRecent"
    }

    object RequestQueries {
        const val METHOD = "method"
        const val API_KEY = "api_key"
        const val FORMAT = "format"
        const val NO_JSON_CALLBACK = "nojsoncallback"
        const val QUERY_TEXT = "text"
        const val EXTRAS = "extras"
        const val SAFE_SEARCH = "safe_search"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"
    }

    const val DEFAULT_PER_PAGE = 20
    const val EMPTY_STRING = ""
    const val STRING_SLASH = "/"
    const val STRING_UNDERSCORE = "_"
    const val PREVIEW_EXTENSION = ".jpg"
    private const val REQUEST_EXTRAS = "original_format,url_c,url_w,url_t"
    private const val NO_JSON_CALLBACK = "1" //means true ¯\_(ツ)_/¯
    private const val DEFAULT_FORMAT = "json"
    private const val SAFE_SEARCH_DEFAULT = "1"
    const val BASE_URL = "https://www.flickr.com/"
    const val BASE_CDN_URL = "https://live.staticflickr.com"
    const val API_ENDPOINT = "services/rest/"
    private const val API_KEY = "ba55f8262a0e541b0defd6f6bb132967"
    const val API_SECRET = "3891f12f426cbab7" //TODO move out of here


    val DEFAULT_QUERY_MAP = mapOf(
        RequestQueries.API_KEY to API_KEY,
        RequestQueries.FORMAT to DEFAULT_FORMAT,
        RequestQueries.NO_JSON_CALLBACK to NO_JSON_CALLBACK,
        RequestQueries.EXTRAS to REQUEST_EXTRAS,
        RequestQueries.SAFE_SEARCH to SAFE_SEARCH_DEFAULT
    )

    const val HTTP_DISK_CACHE_SIZE: Long = 20 * 1024 // 20 Mb in Kb;
    const val HTTP_CLIENT_CONNECTION_TIMEOUT = 30_000L
    const val HTTP_CLIENT_READ_TIMEOUT = 60_000L
    const val HTTP_CLIENT_WRITE_TIMEOUT = 60_000L
}