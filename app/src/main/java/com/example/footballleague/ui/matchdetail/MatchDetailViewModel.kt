package com.example.footballleague.ui.matchdetail

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
import com.example.footballleague.source.remote.DetailMatch
import com.example.footballleague.source.remote.DetailMatchResponse
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MatchDetailViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository,
    private val app: Application
) : ViewModel() {

    private val tag = this.javaClass.simpleName

    private val colorPrimaryLighter = app.getColor(R.color.colorPrimaryLighter)

    private val _detailMatch = MutableLiveData<DetailMatch>()
    val detailMatch: LiveData<DetailMatch>
        get() = _detailMatch

    private val _darkVibrantSwatch = MutableLiveData<Int>()
    val darkVibrantSwatch: LiveData<Int>
        get() = _darkVibrantSwatch

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    private val _addFavoriteResponse = MutableLiveData<String>()
    val addFavoriteResponse: LiveData<String>
        get() = _addFavoriteResponse

    var isFavorite: Boolean = false

    fun getDetailMatch(eventId: String) {
        _isShowLoading.postValue(true)

        GlobalScope.launch {
            val data = gson.fromJson(
                apiRepository.doRequestAsync(TheSportDbApi.getDetailMatch(eventId)).await(),
                DetailMatchResponse::class.java
            )

            _isShowLoading.postValue(false)
            _detailMatch.postValue(data.detailMatch.first())
        }
    }

    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            palette?.let {
                _darkVibrantSwatch.postValue(it.darkVibrantSwatch?.rgb ?: colorPrimaryLighter)
            }
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

    fun addMatchToFavorite() {
        try {
            _detailMatch.value?.let { match ->
                app.database.addMatchToFavorite(match)
            }
            _addFavoriteResponse.postValue(app.getString(R.string.sucess_added_to_favorite))

        } catch (e: SQLiteConstraintException) {
            _addFavoriteResponse.postValue(e.localizedMessage)
        }
    }

    fun removeMatchFromFavorite() {
        try {
            _detailMatch.value?.let { match ->
                app.database.removeMatchFromFavorite(match.idEvent)
            }

            _addFavoriteResponse.postValue(app.getString(R.string.success_remove_to_favorite))
        } catch (e: SQLiteConstraintException) {
            _addFavoriteResponse.postValue(e.localizedMessage)
        }
    }

    fun favoriteState(eventId: String) {
        try {
            isFavorite = app.database.favoriteMatchState(eventId)
        } catch (e: SQLiteConstraintException) {
            Log.e(tag, e.localizedMessage ?: "")
        }
    }
}

class MatchDetailViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository,
    private val app: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchDetailViewModel::class.java)) {
            return MatchDetailViewModel(gson, apiRepository, app) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}