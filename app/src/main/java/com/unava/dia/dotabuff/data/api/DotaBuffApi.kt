package com.unava.dia.dotabuff.data.api

import com.unava.dia.dotabuff.domain.model.AccInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DotaBuffApi {
    @GET("players/{accountId}")
    suspend fun getPlayerInfo(@Path("accountId") accountId: String): Response<AccInformation>
}