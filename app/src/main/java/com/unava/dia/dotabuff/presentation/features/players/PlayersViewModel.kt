package com.unava.dia.dotabuff.presentation.features.players

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.domain.repository.DotaBuffRepository
import com.unava.dia.dotabuff.presentation.features.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val repository: DotaBuffRepository,
) : ViewModel() {
    val state = MutableStateFlow<State>(State.START)
    var isLoading = mutableStateOf(true)

    init {
        getPlayerList()
    }

    fun getPlayerList() = viewModelScope.launch(Dispatchers.IO) {
        //state.value = State.LOADING
        repository.getPlayers().distinctUntilChanged().collect {
            if (it.isNullOrEmpty()) {
                state.value = State.FAILURE("list is empty")
            } else {
                state.value = State.SUCCESSLIST(it)
            }
            delay(100)
            isLoading.value = false
        }
    }

    fun updatePlayer(player: AccInformation) = viewModelScope.launch(Dispatchers.IO) {
        repository.updatePlayer(player)
    }

    fun deletePlayer(player: AccInformation) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletePlayer(player)
    }
}
