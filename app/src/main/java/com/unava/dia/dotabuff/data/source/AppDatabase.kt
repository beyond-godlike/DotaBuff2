package com.unava.dia.dotabuff.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.domain.model.Profile
import com.unava.dia.dotabuff.util.MmrEstimateConverter
import com.unava.dia.dotabuff.util.ProfileConverter

@Database(entities = [AccInformation::class, Profile::class], version = 1, exportSchema = false)
@TypeConverters(MmrEstimateConverter::class, ProfileConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao: PlayerDao

    companion object {
        const val DATABASE_NAME = "players_db"
    }
}