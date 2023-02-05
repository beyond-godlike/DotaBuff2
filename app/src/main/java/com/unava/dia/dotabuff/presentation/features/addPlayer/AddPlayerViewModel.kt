package com.unava.dia.dotabuff.presentation.features.addPlayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unava.dia.dotabuff.data.api.DotaBuffApi
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.domain.repository.DotaBuffRepository
import com.unava.dia.dotabuff.presentation.features.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddPlayerViewModel @Inject constructor(
    private val repository: DotaBuffRepository,
    private val api: DotaBuffApi,

    ) : ViewModel() {

    val state = MutableStateFlow<State>(State.START)

    var playerId by mutableStateOf("")

    fun fetchPlayer() = viewModelScope.launch {
        var response: Response<AccInformation>? = null
        try {
            response = api.getPlayerInfo(playerId)
            state.value = State.SUCCESS(response.body()!!)
        } catch (e: Exception) {
            //state.value = State.FAILURE(response!!.code().toString())
            state.value = State.FAILURE(response?.isSuccessful.toString())
        }
    }

    fun loadPlayer(id: Int) = viewModelScope.launch {
        try {
            repository.getPlayer(id).distinctUntilChanged().collect {
                state.value = State.SUCCESS(it)
                playerId = it.profile?.account_id.toString()
            }
        } catch (e: Exception) {
            //state.value = State.FAILURE(response!!.code().toString())
            state.value = State.FAILURE("player not found")
        }
    }

    fun addPlayer(player: AccInformation) = viewModelScope.launch(Dispatchers.IO) {
        if(player.id == null) {
                repository.insertPlayer(player)
        } else  {
            repository.updatePlayer(player)
        }
    }

    fun deletePlayer(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletePlayer(id)
    }
}