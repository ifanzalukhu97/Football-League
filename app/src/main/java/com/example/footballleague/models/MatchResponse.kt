package com.example.footballleague.models

import com.google.gson.annotations.SerializedName

data class Match(
    val idEvent: String,
    @SerializedName("strEvent") val eventName: String,
    @SerializedName("strHomeTeam") val homeTeamName: String,
    @SerializedName("strAwayTeam") val awayTeamName: String,
    @SerializedName("intHomeScore") val homeScore: String?,
    @SerializedName("intAwayScore") val awayScore: String?,
    @SerializedName("strTime") val timeEvent: String,
    val dateEvent: String,
    val idHomeTeam: String,
    val idAwayTeam: String,
    @SerializedName("strSport") val sportType: String
)

data class MatchResponse(
    @SerializedName("events") val matchList: List<Match>?
)