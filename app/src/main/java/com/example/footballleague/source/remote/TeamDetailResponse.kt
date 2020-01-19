package com.example.footballleague.source.remote

import com.google.gson.annotations.SerializedName

data class TeamDetail(
    @SerializedName("idTeam") val teamId: String,
    @SerializedName("strTeam") val teamName: String,
    @SerializedName("strDescriptionEN") val teamDesc: String,
    @SerializedName("strTeamBadge") val teamBadge: String,
    @SerializedName("strKeywords") val nicknamed: String,
    @SerializedName("strStadium") val stadium: String,
    @SerializedName("intStadiumCapacity") val stadiumCapacity: String,
    @SerializedName("strStadiumLocation") val stadiumLocation: String
)

data class TeamDetailResponse(
    val teams: List<TeamDetail>
)