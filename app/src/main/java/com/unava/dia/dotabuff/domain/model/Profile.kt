package com.unava.dia.dotabuff.domain.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Profile(
    @SerializedName("account_id")
    var account_id: Int? = null,

    @SerializedName("personaname")
    var personaname: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("plus")
    var plus: Boolean? = false,

    @SerializedName("cheese")
    var cheese: Int? = null,

    @SerializedName("steamid")
    var steamid: String? = null,

    @SerializedName("avatar")
    var avatar: String? = null,

    @SerializedName("avatarmedium")
    var avatarmedium: String? = null,

    @SerializedName("avatarfull")
    var avatarfull: String? = null,

    @SerializedName("profileurl")
    var profileurl: String? = null,

    @SerializedName("last_login")
    var last_login: String? = null,

    @SerializedName("loccountrycode")
    var loccountrycode: String? = null,

    @SerializedName("is_contributor")
    var is_contributor: Boolean? = false

) : Serializable