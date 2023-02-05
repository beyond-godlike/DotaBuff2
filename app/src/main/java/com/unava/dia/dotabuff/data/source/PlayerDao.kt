package com.unava.dia.dotabuff.data.source

import androidx.room.*
import com.unava.dia.dotabuff.domain.model.AccInformation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface PlayerDao {
    @Insert
    fun insertPlayer(player: AccInformation): Long?

    @Query("SELECT * from AccInformation")
    fun getPlayers() : Flow<List<AccInformation>>

    @Query("SELECT * from AccInformation")
    fun getPlayersAsync(): List<AccInformation>

    @Query("SELECT * FROM AccInformation WHERE id =:playerId")
    fun getPlayer(playerId: Int): Flow<AccInformation>

    @Query("SELECT * FROM AccInformation WHERE id =:playerId")
    fun getPlayerAsync(playerId: Int): AccInformation

    @Update
    fun updatePlayer(player: AccInformation)

    @Delete
    fun deletePlayer(player: AccInformation)

    @Query("SELECT COUNT(*) FROM AccInformation")
    fun countAllPlayers() : Int

    @Query("SELECT EXISTS (SELECT 1 FROM AccInformation WHERE id = :id)")
    fun isInDatabase(id: Int) : Boolean
}