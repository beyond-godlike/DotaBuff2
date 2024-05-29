package com.unava.dia.dotabuff.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import javax.inject.Named
import com.unava.dia.dotabuff.data.api.DotaBuffApi
import com.unava.dia.dotabuff.data.api.RetrofitFactory

@Module
@InstallIn(SingletonComponent::class)
object RestModule {

    @Provides
    @Singleton
    @Named("openApiRetrofit")
    fun provideOpenApiRetrofit(): Retrofit {
        return RetrofitFactory.retrofit("https://api.opendota.com/api/")
    }

    @Provides
    @Singleton
    @Named("steamRetrofit")
    fun provideSteamRetrofit(): Retrofit {
        return RetrofitFactory.retrofit("https://api.steampowered.com/")
    }


    @Provides
    @Singleton
    @Named("openApi")
    fun provideOpenApi(@Named("openApiRetrofit") retrofit: Retrofit): DotaBuffApi {
        return retrofit.create(DotaBuffApi::class.java)
    }

    @Provides
    @Singleton
    @Named("steamApi")
    fun provideSteamApi(@Named("steamRetrofit") retrofit: Retrofit): DotaBuffApi {
        return retrofit.create(DotaBuffApi::class.java)
    }

}