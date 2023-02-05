package com.unava.dia.dotabuff.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.unava.dia.dotabuff.domain.model.MmrEstimate

object MmrEstimateConverter {
    @TypeConverter
    fun toObject(value: String) : MmrEstimate {
        val listType = object : TypeToken<MmrEstimate>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun toString(value: MmrEstimate?) : String {
        return Gson().toJson(value)
    }
}