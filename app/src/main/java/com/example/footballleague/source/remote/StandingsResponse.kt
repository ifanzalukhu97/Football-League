package com.example.footballleague.source.remote

import com.google.gson.annotations.SerializedName

data class Standings(
    @SerializedName("name") val teamName: String,
    val played: Int,
    @SerializedName("win") val won: Int,
    val draw: Int,
    val loss: Int,
    @SerializedName("goalsdifference") val goalsDifference: Int,
    @SerializedName("total") val points: Int,
    @SerializedName("teamid") val teamId: String
)

data class StandingsResponse(
    @SerializedName("table") val standings: List<Standings>
)