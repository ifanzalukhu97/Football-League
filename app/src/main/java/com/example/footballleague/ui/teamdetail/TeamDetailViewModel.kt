package com.example.footballleague.ui.teamdetail

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.source.local.db.database
import com.example.footballleague.source.remote.TeamDetail
import com.example.footballleague.source.remote.TeamDetailResponse
import com.example.footballleague.utils.asFavoriteTeamEntity
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamDetailViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository,
    private val app: Application
) : ViewModel() {

    private val tag = this.javaClass.simpleName

    private val colorPrimaryLighter = app.getColor(R.color.colorPrimaryLighter)
    private val _darkVibrantSwatch = MutableLiveData<Int>()
    val darkVibrantSwatch: LiveData<Int>
        get() = _darkVibrantSwatch

    private val _teamDetail = MutableLiveData<TeamDetail>()
    val teamDetail: LiveData<TeamDetail>
        get() = _teamDetail

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    private val _addFavoriteResponse = MutableLiveData<String>()
    val addFavoriteResponse: LiveData<String>
        get() = _addFavoriteResponse

    var isFavorite: Boolean = false

    fun getDetailTeam(teamId: String) {
        GlobalScope.launch {
            _isShowLoading.postValue(true)

            val data = gson.fromJson(
                apiRepository.doRequestAsync(
                    TheSportDbApi.getDetailTeam(teamId)
                ).await(),
                TeamDetailResponse::class.java
            )

            _isShowLoading.postValue(false)
            _teamDetail.postValue(data.teams.first())
        }
    }

    fun getImageBitmap(imageUrl: String) {
        Glide.with(app)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    createPaletteAsync(resource)
                }

            })
    }

    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            palette?.let {
                _darkVibrantSwatch.postValue(it.darkVibrantSwatch?.rgb ?: colorPrimaryLighter)
            }
        }
    }

    fun addTeamToFavorite() {
        try {
            _teamDetail.value?.let { teamDetail ->
                app.database.addTeamToFavorite(teamDetail.asFavoriteTeamEntity())
            }
            _addFavoriteResponse.postValue(app.getString(R.string.sucess_added_to_favorite))

        } catch (e: SQLiteConstraintException) {
            _addFavoriteResponse.postValue(e.localizedMessage)
        }
    }

    fun removeTeamFromFavorite() {
        try {
            _teamDetail.value?.let { teamDetail ->
                app.database.removeTeamFromFavorite(teamDetail.teamId)
            }

            _addFavoriteResponse.postValue(app.getString(R.string.success_remove_to_favorite))
        } catch (e: SQLiteConstraintException) {
            _addFavoriteResponse.postValue(e.localizedMessage)
        }
    }

    fun favoriteState(teamId: String) {
        try {
            isFavorite = app.database.favoriteTeamState(teamId)
        } catch (e: SQLiteConstraintException) {
            Log.e(tag, e.localizedMessage ?: "")
        }
    }
}

class TeamDetailViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository,
    private val app: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamDetailViewModel::class.java)) {
            return TeamDetailViewModel(gson, apiRepository, app) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}