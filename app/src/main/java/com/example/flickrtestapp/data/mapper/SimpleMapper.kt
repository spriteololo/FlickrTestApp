package com.example.flickrtestapp.data.mapper

abstract class SimpleConverter<FROM, TO>(
    override val fromClass: Class<FROM>,
    override val toClass: Class<TO>
) : Converter<FROM, TO>