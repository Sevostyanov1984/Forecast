package com.example.forecastmvvm.data.db.converters

import androidx.room.TypeConverter
import org.apache.commons.lang3.StringUtils
import java.util.*

class ListToStringConverter {
    @TypeConverter
    fun fromData(data: List<String>): String? {
        return StringUtils.join(data, ';')
    }

    @TypeConverter
    fun toData(data: String?): List<Array<String>>? {
        return Arrays.asList(data?.split(";".toRegex())?.toTypedArray()) as List<Array<String>>?
    }
}