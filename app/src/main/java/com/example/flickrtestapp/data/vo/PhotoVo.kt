package com.example.flickrtestapp.data.vo

import android.os.Parcelable
import com.example.flickrtestapp.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoVo(
    var id: String,
    val owner: String,
    var secret: String,
    var server: String,
    val width: Int,
    val height: Int,
    val originalFormat: String? = null,
    val originalSecret: String? = null,
    var title: String,
    var biggestPreviewSize: String
) : Parcelable {

    fun getImageRatio(): String {
        return "$width:$height"
    }

    fun getUrl(): String {
        return Constants.BASE_CDN_URL + Constants.STRING_SLASH + //https://live.staticflickr.com/
                server + Constants.STRING_SLASH + // 65536/
                id + Constants.STRING_UNDERSCORE + // 51241099382_
                secret + Constants.STRING_UNDERSCORE + // 7eb1ace6fd_
                biggestPreviewSize + Constants.PREVIEW_EXTENSION // w.jpg
    }
}
