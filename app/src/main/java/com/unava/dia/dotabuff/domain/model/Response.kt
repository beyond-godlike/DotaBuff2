package com.unava.dia.dotabuff.domain.model

import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("players")
    var players: ArrayList<Player>? = null,
)