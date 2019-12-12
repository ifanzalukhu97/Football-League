package com.example.footballleague.source.remote

import com.google.gson.annotations.SerializedName

data class SearchMatchResponse(
    @SerializedName("event") val matchList: List<Match>?
)