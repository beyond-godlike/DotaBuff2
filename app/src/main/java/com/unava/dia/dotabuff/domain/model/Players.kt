package com.unava.dia.dotabuff.domain.model

import com.google.gson.annotations.SerializedName

data class Players (
    @SerializedName("response")
    var response: Response? = null
)