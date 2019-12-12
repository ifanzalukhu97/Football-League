package com.example.footballleague.source.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class League(
    val id: Int,
    val name: String,
    val description: String,
    val badge: Int
) : Parcelable