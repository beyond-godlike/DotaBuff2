package com.unava.dia.dotabuff.di

import android.app.Application
import androidx.room.Room
import com.unava.dia.dotabuff.data.repository.DotaBuffRepositoryImpl
import com.unava.dia.dotabuff.data.source.AppDatabase
import com.unava.dia.dotabuff.domain.repository.DotaBuffRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDotaBuffRepository(database: AppDatabase): DotaBuffRepository {
        return DotaBuffRepositoryImpl(database.dao)
    }
}