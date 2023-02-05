package com.unava.dia.dotabuff.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.unava.dia.dotabuff.domain.model.Profile


object ProfileConverter {
    @TypeConverter
    fun toObject(value: String) : Profile {
        val listType = object : TypeToken<Profile?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun toString(value: Profile?) : String {
        return Gson().toJson(value)
    }
}