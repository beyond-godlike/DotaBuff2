package com.unava.dia.dotabuff.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import com.unava.dia.dotabuff.data.api.DotaBuffApi
import com.unava.dia.dotabuff.data.api.RetrofitFactory

@Module
@InstallIn(SingletonComponent::class)
object RestModule {
    @Provides
    fun provideBaseUrl() = "https://api.opendota.com/api/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl:String): Retrofit {
        return RetrofitFactory.retrofit(baseUrl)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(DotaBuffApi::class.java)
}