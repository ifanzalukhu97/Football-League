package com.example.footballleague.source.remote

import com.google.gson.annotations.SerializedName

data class DetailMatchResponse(
    @SerializedName("events") val detailMatch: List<DetailMatch>
)

data class DetailMatch(
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

    @SerializedName("intHomeShots") val homeShots: Int?,
    @SerializedName("intAwayShots") val awayShots: Int?,

    @SerializedName("strAwayYellowCards") val awayYellowCards: String?,
    @SerializedName("strHomeYellowCards") val homeYellowCards: String?,
    @SerializedName("strHomeRedCards") val homeRedCards: String?,
    @SerializedName("strAwayRedCards") val awayRedCards: String?,

    @SerializedName("strHomeGoalDetails") val homeGoalDetails: String?,
    @SerializedName("strAwayGoalDetails") val awayGoalDetails: String?
) {
    val isNextMatch: Boolean
        get() = homeScore.isNullOrBlank() && awayScore.isNullOrBlank()
}