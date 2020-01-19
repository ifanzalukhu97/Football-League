package com.example.footballleague.source.local.entity

data class FavoriteMatchEntity(
    val idEvent: String,
    val eventName: String,
    val homeTeamName: String,
    val awayTeamName: String,
    val homeScore: String?,
    val awayScore: String?,
    val timeEvent: String,
    val dateEvent: String,
    val idHomeTeam: String,
    val idAwayTeam: String,
    val isNextMatch: Boolean
) {
    companion object {
        const val TABLE_FAVORITE_MATCH: String = "TABLE_FAVORITE_MATCH"
        const val ID_EVENT: String = "ID_EVENT"
        const val EVENT_NAME: String = "EVENT_NAME"
        const val HOME_TEAM_NAME: String = "HOME_TEAM_NAME"
        const val AWAY_TEAM_NAME: String = "AWAY_TEAM_NAME"
        const val HOME_SCORE: String = "HOME_SCORE"
        const val AWAY_SCORE: String = "AWAY_SCORE"
        const val TIME_EVENT: String = "TIME_EVENT"
        const val DATE_EVENT: String = "DATE_EVENT"
        const val ID_HOME_TEAM: String = "ID_HOME_TEAM"
        const val ID_AWAY_TEAM: String = "ID_AWAY_TEAM"
        const val IS_NEXT_MATCH: String = "IS_NEXT_MATCH"
    }
}