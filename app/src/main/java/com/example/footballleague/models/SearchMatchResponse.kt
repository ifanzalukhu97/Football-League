package com.example.footballleague.models

import com.google.gson.annotations.SerializedName

data class SearchMatchResponse(
    @SerializedName("event") val matchList: List<Match>?
)