package com.example.newswave.db

import androidx.room.TypeConverter
import com.example.newswave.model.Source

class Converters {

    // Превращаем Source в String (для сохранения в базу)
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    // Превращаем String обратно в Source (когда читаем из базы)
    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name) // id и name будут одинаковыми, это не критично
    }
}