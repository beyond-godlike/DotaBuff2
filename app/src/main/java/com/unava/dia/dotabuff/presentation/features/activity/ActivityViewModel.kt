package com.unava.dia.dotabuff.presentation.features.activity

import androidx.lifecycle.viewModelScope
import com.unava.dia.dotabuff.data.api.DotaBuffApi
import com.unava.dia.dotabuff.domain.model.Player
import com.unava.dia.dotabuff.presentation.core.BaseViewModel
import com.unava.dia.dotabuff.presentation.core.ViewAction
import com.unava.dia.dotabuff.presentation.core.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ActivityViewModel @Inject constructor(
    @Named("steamApi") private val steamApi: DotaBuffApi,
) : BaseViewModel<ActivityViewModel.State, ActivityViewModel.Action>(
    initialState = State.START
) {
    val apiKey = Properties().apply {
        load(FileInputStream(File("local.properties")))
    }.getProperty("api_key")

    override fun dispatch(action: Action) {
        when (action) {
            is Action.FetchActivity -> {
                viewModelScope.launch {
                    val response = steamApi.getPlayerState(
                        apiKey,
                        action.steamId
                    )

                    if (response.isSuccessful && response.body() != null) {
                        updateState(State.SUCCESS(response.body()!!.response?.players?.get(0)!!))
                    } else {
                        updateState(State.FAILURE(response.code().toString() + response.body()
                            .toString()))
                    }
                }
            }
        }
    }


    sealed class State : ViewState {
        object START : State()
        object LOADING : State()
        data class SUCCESS(val player: Player) : State()
        data class FAILURE(val message: String) : State()
    }

    sealed class Action : ViewAction {
        //data class AddPlayer(val player: AccInformation) : Action()
        //data class DeletePlayer(val id: Int) : Action()
        //data class LoadPlayer(val id: Int) : Action()
        //object FetchActivity : Action()
        data class FetchActivity(val steamId: String) : Action()
    }

}