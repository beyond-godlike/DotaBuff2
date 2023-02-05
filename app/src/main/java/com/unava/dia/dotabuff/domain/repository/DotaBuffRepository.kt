package com.unava.dia.dotabuff.domain.repository

import com.unava.dia.dotabuff.domain.model.AccInformation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DotaBuffRepository {
    fun getPlayers(): Flow<List<AccInformation>>

    fun getPlayer(id: Int): Flow<AccInformation>

    suspend fun insertPlayer(player: AccInformation)

    suspend fun updatePlayer(player: AccInformation)

    suspend fun updatePlayer(id: Int)

    suspend fun deletePlayer(player: AccInformation)

    suspend fun deletePlayer(id: Int)

    suspend fun isInDatabase(id: Int) : Boolean

}