package com.example.flickrtestapp.util

import com.example.flickrtestapp.data.mapper.TypeMapper
import com.example.flickrtestapp.data.mapper.converter.PhotoResponseToPhotoVoConverter

object FlickrTypeMapper {
    fun initConverters() {
        val converters = listOf(
            PhotoResponseToPhotoVoConverter()
        )
        TypeMapper.registerConverters(converters)
    }
}