package com.example.footballleague.source.remote

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("idTeam") val teamId: String,
    @SerializedName("strTeam") val teamName: String,
    @SerializedName("strDescriptionEN") val teamDesc: String,
    @SerializedName("strTeamBadge") val teamBadge: String,
    @SerializedName("strSport") val sportType: String = "Soccer"
)

data class TeamsResponse(
    val teams: List<Team>
)