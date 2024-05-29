package com.unava.dia.dotabuff.data.api

import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.domain.model.Players
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DotaBuffApi {
    @GET("players/{accountId}")
    suspend fun getPlayerInfo(@Path("accountId") accountId: String): Response<AccInformation>
    @GET("ISteamUser/GetPlayerSummaries/v0002/")
    suspend fun getPlayerState(
        @Query("key")key: String,
        @Query("steamids") steamids: String
    ) : Response<Players>

    //@GET("IDOTA2Match_570/GetMatchHistory/V001/")
    //suspend fun getHistory(@Query("account_id") accountId: String,
    //                       @Query("key") key: String) : Response<History>
}