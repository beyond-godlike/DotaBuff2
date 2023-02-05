package com.unava.dia.dotabuff.presentation.features

import com.unava.dia.dotabuff.domain.model.AccInformation
import kotlinx.coroutines.flow.Flow

sealed class State {
    object START : State()
    object LOADING : State()
    data class SUCCESSLIST(val players: List<AccInformation>) : State()
    data class SUCCESS(val player: AccInformation): State()
    data class FAILURE(val message: String) : State()
}