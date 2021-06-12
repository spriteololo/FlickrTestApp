package com.example.flickrtestapp.data.mapper

import java.util.*

object TypeMapper {
    private val CONVERTERS: MutableList<Converter<*, *>> =
        Collections.synchronizedList(mutableListOf())

    fun <FROM : Any, TO> convert(value: FROM, toClass: Class<TO>): TO {
        val aClass = value::class.java
        return getConverter(aClass, toClass).convert(value)
    }

    fun <FROM : Any, TO> convertAsList(value: List<FROM>, toClass: Class<TO>): List<TO> {
        return if (value.isEmpty()) {
            emptyList()
        } else {
            val firstItem = value.component1()
            val aClass = firstItem::class.java
            val converter = getConverter(aClass, toClass)
            value.map { converter.convert(it) }
        }
    }


    private fun <FROM, TO> getConverter(
        fromClass: Class<out FROM>,
        toClass: Class<TO>
    ): Converter<FROM, TO> {
        return findConverter(fromClass, toClass)
            ?: throw UnsupportedOperationException(
                String.format(
                    "Converter %s -> %s not registered",
                    fromClass,
                    toClass
                )
            )
    }

    private fun <FROM, TO> findConverter(
        fromClass: Class<out FROM>,
        toClass: Class<TO>
    ): Converter<FROM, TO>? {
        for (converter in CONVERTERS) {
            if (isClassesEquals(converter.fromClass, fromClass)
                && isClassesEquals(converter.toClass, toClass)
            ) {
                return converter as? Converter<FROM, TO>
            }
        }
        return null
    }

    fun registerConverters(converters: List<Converter<*, *>>) {
        for (converter in converters) {
            if (!exists(converter)) {
                CONVERTERS.add(converter)
            }
        }
    }

    private fun exists(converter: Converter<*, *>): Boolean {
        for (c in CONVERTERS) {
            if (isConvertersEquals(c, converter)) {
                return true
            }
        }
        return false
    }

    private fun isConvertersEquals(
        converter1: Converter<*, *>,
        converter2: Converter<*, *>
    ): Boolean {
        val from1: Class<*> = converter1.fromClass
        val to1: Class<*> = converter1.toClass
        val from2: Class<*> = converter2.fromClass
        val to2: Class<*> = converter2.toClass
        return isClassesEquals(from1, from2) && isClassesEquals(to1, to2)
    }

    private fun isClassesEquals(
        class1: Class<*>,
        class2: Class<*>
    ): Boolean {
        return class1 == class2
    }
}