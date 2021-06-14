package com.example.flickrtestapp.data.mapper.converter

import com.example.flickrtestapp.Constants.ImageSizeSuffix
import com.example.flickrtestapp.data.apimodel.ServerPhoto
import com.example.flickrtestapp.data.mapper.SimpleConverter
import com.example.flickrtestapp.data.vo.PhotoVo


class PhotoResponseToPhotoVoConverter : SimpleConverter<ServerPhoto, PhotoVo>(
    ServerPhoto::class.java,
    PhotoVo::class.java
) {
    override fun convert(value: ServerPhoto): PhotoVo {
        val biggestPreviewSize =
            when {
                value.heightC != null -> ImageSizeSuffix.SUFFIX_C
                value.heightW != null -> ImageSizeSuffix.SUFFIX_W
                else -> ImageSizeSuffix.SUFFIX_T
            }

        return PhotoVo(
            value.id,
            value.owner,
            value.secret,
            value.server,
            value.widthC ?: value.widthW ?: value.widthT,
            value.heightC ?: value.heightW ?: value.heightT,
            value.originalFormat,
            value.originalSecret,
            value.title,
            biggestPreviewSize
        )
    }
}