package com.example.footballleague.models

import com.google.gson.annotations.SerializedName

data class TeamBadgeResponse(
    val teams: List<TeamBadge>
)

data class TeamBadge(
    @SerializedName("strTeamBadge") val teamBadgeUrl: String,
    @SerializedName("idTeam") val teamId: String
)