package com.example.footballleague.utils

import com.example.footballleague.ApiRepository
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.source.remote.TeamBadge
import com.example.footballleague.source.remote.TeamBadgeResponse
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

object Commons {
    private lateinit var gson: Gson
    private lateinit var apiRepository: ApiRepository

    /**
     * store url team badge,
     * so doesn't every time request team badge url
     * from Team Details
     */
    val listTeamBadge: MutableList<TeamBadge> = mutableListOf()

    fun convertTimeToGmtPlus7(date: String, time: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")

        val dateTimeInString = "$date $time"
        val dateTime = formatter.parse(dateTimeInString)

        val newFormatter = SimpleDateFormat("dd MMM yyy, H:mm a", Locale.getDefault())
        newFormatter.timeZone = TimeZone.getTimeZone("GMT+7")

        return when (dateTime) {
            null -> ""
            else -> newFormatter.format(dateTime)
        }
    }

    fun getTeamBadgeUrl(teamId: String): String {
        val badgeUrl: String

        // initialize gson and apiRepository variable if
        // hasn't initialize it before
        if (!::gson.isInitialized || !::apiRepository.isInitialized) {
            gson = Gson()
            apiRepository = ApiRepository()
        }

        val data = gson.fromJson(
            apiRepository.doRequest(
                TheSportDbApi.getTeamBadgeById(teamId)
            ),
            TeamBadgeResponse::class.java
        )

        badgeUrl = data.teams.first().teamBadgeUrl
        listTeamBadge.add(
            TeamBadge(
                teamId = teamId,
                teamBadgeUrl = badgeUrl
            )
        )

        return badgeUrl
    }
}