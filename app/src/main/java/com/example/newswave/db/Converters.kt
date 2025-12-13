package com.example.newswave.db

import androidx.room.TypeConverter
import com.example.newswave.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name) // id и name будут одинаковыми, это не критично
    }
}