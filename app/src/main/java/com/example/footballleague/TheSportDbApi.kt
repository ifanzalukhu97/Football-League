package com.example.footballleague

import java.util.*

object TheSportDbApi {

    private const val URL_PATH = "api/v1/json/1"

    fun getLeagueDetails(leagueId: String): String {
        return BuildConfig.BASE_URL + URL_PATH + "/lookupleague.php?id=" + leagueId
    }

    fun getLastMatch(eventId: String): String {
        return BuildConfig.BASE_URL + URL_PATH + "/eventspastleague.php?id=" + eventId
    }

    fun getNextMatch(eventId: String): String {
        return BuildConfig.BASE_URL + URL_PATH + "/eventsnextleague.php?id=" + eventId
    }

    fun getTeamBadgeById(teamId: String): String {
        return BuildConfig.BASE_URL + URL_PATH + "/lookupteam.php?id=" + teamId
    }

    fun getDetailMatch(eventId: String): String {
        return BuildConfig.BASE_URL + URL_PATH + "/lookupevent.php?id=" + eventId
    }

    fun searchMatch(query: String): String {
        return BuildConfig.BASE_URL + URL_PATH + "/searchevents.php?e=" + query.replace(" ", "%20")
    }

    fun getStandings(leagueId: String, season: String? = null): String {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).minus(2000)
        val currentSeason = season ?: "${currentYear}${currentYear + 1}"
        return "${BuildConfig.BASE_URL}${URL_PATH}/lookuptable.php?l=${leagueId}&s=${currentSeason}"
    }

    fun getAllTeams(leagueId: String): String {
        return "${BuildConfig.BASE_URL}${URL_PATH}/lookup_all_teams.php?id=${leagueId}"
    }

    fun getDetailTeam(teamId: String): String {
        return "${BuildConfig.BASE_URL}${URL_PATH}/lookupteam.php?id=${teamId}"
    }

    fun searchTeam(query: String): String {
        return BuildConfig.BASE_URL + URL_PATH + "/searchteams.php?t=" + query.replace(" ", "%20")
    }
}