package com.unava.dia.dotabuff.domain.model

import com.google.gson.annotations.SerializedName

data class Player (
    @SerializedName("steamid")
    var steamid: String? = null,

    @SerializedName("personaname")
    var personname: String? = null,

    @SerializedName("avatarmedium")
    var avatarmedium: String? = null,

    @SerializedName("personastate")
    var personastate: Int = -1,

    @SerializedName("gameextrainfo") // Dota 2
    var gameextrainfo: String? = null
)