package com.example.footballleague.source.local.entity

data class FavoriteTeamEntity(
    val teamId: String,
    val teamName: String,
    val teamDesc: String,
    val teamBadge: String
) {
    companion object {
        const val TABLE_FAVORITE_TEAM: String = "TABLE_FAVORITE_TEAM"
        const val TEAM_ID: String = "TEAM_ID"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val TEAM_DESC: String = "TEAM_DESC"
        const val TEAM_BADGE: String = "TEAM_BADGE"

    }
}