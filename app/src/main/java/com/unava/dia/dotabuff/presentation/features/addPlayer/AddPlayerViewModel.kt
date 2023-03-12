package com.unava.dia.dotabuff.presentation.features.addPlayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.unava.dia.dotabuff.data.api.DotaBuffApi
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.domain.repository.DotaBuffRepository
import com.unava.dia.dotabuff.presentation.core.BaseViewModel
import com.unava.dia.dotabuff.presentation.core.ViewAction
import com.unava.dia.dotabuff.presentation.core.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddPlayerViewModel @Inject constructor(
    private val repository: DotaBuffRepository,
    private val api: DotaBuffApi,

    ) : BaseViewModel<AddPlayerViewModel.State, AddPlayerViewModel.Action>(
    initialState = State.START
) {

    var playerId by mutableStateOf("")

    override fun dispatch(action: Action) {
        when (action) {
            is Action.FetchPlayer -> {
                viewModelScope.launch {
                    var response: Response<AccInformation>? = null
                    try {
                        response = api.getPlayerInfo(playerId)
                        updateState(State.SUCCESS(response.body()!!))
                    } catch (e: Exception) {
                        updateState(State.FAILURE(response?.isSuccessful.toString()))
                    }
                }
            }
            is Action.AddPlayer -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (action.player.id == null) {
                        repository.insertPlayer(action.player)
                    } else {
                        repository.updatePlayer(action.player)
                    }
                }
            }
            is Action.DeletePlayer -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deletePlayer(action.id)
                }
            }
            is Action.LoadPlayer -> {
                viewModelScope.launch {
                    try {
                        repository.getPlayer(action.id).distinctUntilChanged().collect {
                            updateState(State.SUCCESS(it))
                            playerId = it.profile?.account_id.toString()
                        }
                    } catch (e: Exception) {
                        //State.FAILURE(response!!.code().toString())
                        updateState(State.FAILURE("player not found"))
                    }
                }
            }
        }
    }


    sealed class State : ViewState {
        object START : State()
        object LOADING : State()
        data class SUCCESS(val player: AccInformation) : State()
        data class FAILURE(val message: String) : State()
    }

    sealed class Action : ViewAction {
        data class AddPlayer(val player: AccInformation) : Action()
        data class DeletePlayer(val id: Int) : Action()
        data class LoadPlayer(val id: Int) : Action()
        object FetchPlayer : Action()
    }
}