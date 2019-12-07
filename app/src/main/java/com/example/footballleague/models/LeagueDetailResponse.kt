package com.example.footballleague.models

import com.google.gson.annotations.SerializedName

data class LeagueDetail(
    @SerializedName("strLeague") val name: String,
    @SerializedName("strDescriptionEN") val description: String,
    @SerializedName("intFormedYear") val formed: String,
    @SerializedName("strCountry") val country: String,
    @SerializedName("strBadge") val badgeUrl: String,
    @SerializedName("strBanner") val bannerUrl: String,
    @SerializedName("strFanart1") val fanArtUrl: String,

    @SerializedName("strWebsite") val websiteUrl: String,
    @SerializedName("strFacebook") val facebookUrl: String,
    @SerializedName("strTwitter") val twitterUrl: String,
    @SerializedName("strYoutube") val youtubeUrl: String,
    @SerializedName("strTrophy") val trophyImgUrl: String
)

data class LeagueDetailResponse(
    val leagues: List<LeagueDetail>
)