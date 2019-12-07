package com.example.footballleague

import android.net.Uri

object TheSportDbApi {

    private const val URL_PATH = "api/v1/json/1"

    fun getLeagueDetails(leagueId: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(URL_PATH)
            .appendPath("lookupleague.php")
            .appendQueryParameter("id", leagueId)
            .build()
            .toString()
    }

    fun getLastMatch(eventId: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(URL_PATH)
            .appendPath("eventspastleague.php")
            .appendQueryParameter("id", eventId)
            .build()
            .toString()
    }

    fun getNextMatch(eventId: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(URL_PATH)
            .appendPath("eventsnextleague.php")
            .appendQueryParameter("id", eventId)
            .build()
            .toString()
    }

    fun getTeamBadgeById(teamId: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(URL_PATH)
            .appendPath("lookupteam.php")
            .appendQueryParameter("id", teamId)
            .build()
            .toString()
    }

    fun getDetailMatch(eventId: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(URL_PATH)
            .appendPath("lookupevent.php")
            .appendQueryParameter("id", eventId)
            .build()
            .toString()
    }

    fun searchMatch(query: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(URL_PATH)
            .appendPath("searchevents.php")
            .appendQueryParameter("e", query)
            .build()
            .toString()
    }
}