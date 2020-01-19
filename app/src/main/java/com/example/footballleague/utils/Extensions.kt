package com.example.footballleague.utils

import com.example.footballleague.source.local.entity.FavoriteMatchEntity
import com.example.footballleague.source.local.entity.FavoriteTeamEntity
import com.example.footballleague.source.remote.Match
import com.example.footballleague.source.remote.Team
import com.example.footballleague.source.remote.TeamDetail

fun List<FavoriteMatchEntity>.asListMatch(): List<Match> {
    return this.map {
        Match(
            it.idEvent,
            it.eventName,
            it.homeTeamName,
            it.awayTeamName,
            it.homeScore,
            it.awayScore,
            it.timeEvent,
            it.dateEvent,
            it.idHomeTeam,
            it.idAwayTeam
        )
    }
}

fun TeamDetail.asFavoriteTeamEntity(): FavoriteTeamEntity {
    return with(this) {
        FavoriteTeamEntity(
            teamId = teamId,
            teamName = teamName,
            teamDesc = teamDesc,
            teamBadge = teamBadge
        )
    }
}

fun List<FavoriteTeamEntity>.asTeam(): List<Team> {
    return this.map {
        Team(
            teamId = it.teamId,
            teamName = it.teamName,
            teamDesc = it.teamDesc,
            teamBadge = it.teamBadge
        )
    }
}


