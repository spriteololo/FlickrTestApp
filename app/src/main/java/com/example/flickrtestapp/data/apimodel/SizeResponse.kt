package com.example.flickrtestapp.data.apimodel

import com.google.gson.annotations.SerializedName

data class SizeResponse(
    val sizes: SizeWrapper
)

data class SizeWrapper(
    val canblog: Int,
    val canprint: Int,
    val candownload: Int,
    @SerializedName("size") val sizeList: List<ServerSize>
)

data class ServerSize(
    val width: Int,
    val height: Int,
    val source: String,
)