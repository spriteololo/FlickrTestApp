package com.example.flickrtestapp.data.mapper.converter

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
                value.heightC != null -> "c"
                value.heightW != null -> "w"
                else -> "t"
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