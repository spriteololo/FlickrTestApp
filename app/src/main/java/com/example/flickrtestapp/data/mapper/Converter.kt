package com.example.flickrtestapp.data.mapper

interface Converter<FROM, TO> {

    val fromClass: Class<FROM>

    val toClass: Class<TO>

    fun convert(value: FROM): TO
}