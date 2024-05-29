package com.unava.dia.dotabuff.data.repository

import com.unava.dia.dotabuff.data.source.PlayerDao
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.domain.repository.DotaBuffRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class DotaBuffRepositoryImpl(private val dao: PlayerDao) : DotaBuffRepository {

    override fun getPlayers(): Flow<List<AccInformation>> = dao.getPlayers().flowOn(Dispatchers.IO)
        .conflate()

    override fun getPlayer(id: Int) = dao.getPlayer(id).flowOn(Dispatchers.IO).conflate()

    override suspend fun insertPlayer(player: AccInformation) {
        dao.insertPlayer(player)
    }

    override suspend fun updatePlayer(player: AccInformation) {
        dao.updatePlayer(player)
    }

    override suspend fun updatePlayer(id: Int) {
        dao.updatePlayer(getPlayer(id).single())
    }

    override suspend fun deletePlayer(player: AccInformation) {
        dao.deletePlayer(player)
    }

    override suspend fun deletePlayer(id: Int) {
        dao.deletePlayer(getPlayer(id).first())
    }

    override suspend fun isInDatabase(steamid: String) : Boolean {
        return dao.isInDatabase(steamid)
    }

}