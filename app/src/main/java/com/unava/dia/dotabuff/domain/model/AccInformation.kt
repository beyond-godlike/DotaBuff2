package com.unava.dia.dotabuff.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class AccInformation(
    @SerializedName("tracked_until")
    var tracked_until: String? = null,
    @SerializedName("solo_competitive_rank")
    var solo_competitive_rank: String? = null,
    @SerializedName("competitive_rank")
    var competitive_rank: String? = null,
    @SerializedName("rank_tier")
    var rank_tier: Int? = null,
    @SerializedName("leaderboard_rank")
    var leaderboard_rank: String? = null,
    @SerializedName("mmr_estimate")
    var mmr_estimate: MmrEstimate? = null,
    @SerializedName("profile")
    var profile: Profile? = null
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}