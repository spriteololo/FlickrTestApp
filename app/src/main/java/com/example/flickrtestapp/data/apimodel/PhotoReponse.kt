package com.example.flickrtestapp.data.apimodel

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("photos")
    val photoSearchWrapper: PhotoSearchWrapper,
    val stat: String
)

data class PhotoSearchWrapper(
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val total: Int,
    @SerializedName("photo")
    val photos: List<ServerPhoto>
)

data class ServerPhoto(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val title: String,
    @SerializedName("originalsecret") val originalSecret: String? = null,
    @SerializedName("originalformat") val originalFormat: String? = null,

    @SerializedName("url_c") val urlC: String? = null,
    @SerializedName("height_c") val heightC: Int? = null,
    @SerializedName("width_c") val widthC: Int? = null,

    @SerializedName("url_w") val urlW: String? = null,
    @SerializedName("height_w") val heightW: Int? = null,
    @SerializedName("width_w") val widthW: Int? = null,

    @SerializedName("url_t") val urlT: String,
    @SerializedName("height_t") val heightT: Int,
    @SerializedName("width_t") val widthT: Int,
)