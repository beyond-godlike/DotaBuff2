package com.unava.dia.dotabuff.presentation.features.players

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.domain.repository.DotaBuffRepository
import com.unava.dia.dotabuff.presentation.core.BaseViewModel
import com.unava.dia.dotabuff.presentation.core.ViewAction
import com.unava.dia.dotabuff.presentation.core.ViewState
import com.unava.dia.dotabuff.presentation.features.addPlayer.AddPlayerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val repository: DotaBuffRepository,
) : BaseViewModel<PlayersViewModel.State, PlayersViewModel.Action>(
    initialState = State.START
) {
    var isLoading = mutableStateOf(true)

    init {
        dispatch(Action.GetPlayersList)
    }

    override fun dispatch(action: Action) {
        when(action) {
            is Action.UpdatePlayer -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updatePlayer(action.player)
                }
            }
            is Action.DeletePlayer -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deletePlayer(action.player)
                }
            }
            is Action.GetPlayersList -> {
                viewModelScope.launch(Dispatchers.IO) {
                    //updateState(State.LOADING)
                    repository.getPlayers().distinctUntilChanged().collect {
                        if (it.isNullOrEmpty()) {
                            updateState(State.FAILURE("list is empty"))
                        } else {
                            updateState(State.SUCCESS(it))
                        }
                        delay(100)
                        isLoading.value = false
                    }
                }
            }
        }
    }


    sealed class State : ViewState {
        object START : State()
        object LOADING : State()
        data class SUCCESS(val players: List<AccInformation>) : State()
        data class FAILURE(val message: String) : State()
    }

    sealed class Action : ViewAction {
        data class UpdatePlayer(val player: AccInformation) : Action()
        data class DeletePlayer(val player: AccInformation) : Action()
        object GetPlayersList : Action()
    }
}
