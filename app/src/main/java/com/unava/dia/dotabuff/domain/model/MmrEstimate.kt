package com.unava.dia.dotabuff.domain.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class MmrEstimate(
    @SerializedName("estimate")
    var estimate: String? = null,

    @SerializedName("std_dev")
    var std_dev: Int? = null,

    @SerializedName("n")
    var n: Int? = null

) : Serializable