package com.example.footballleague.utils

import com.example.footballleague.source.local.entity.FavoriteEntity
import com.example.footballleague.source.remote.Match

fun List<FavoriteEntity>.asListMatch(): List<Match> {
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


