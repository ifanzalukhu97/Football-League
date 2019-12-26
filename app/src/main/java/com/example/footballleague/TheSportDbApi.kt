package com.example.footballleague

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
}