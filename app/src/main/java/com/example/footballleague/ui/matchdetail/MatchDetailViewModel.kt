package com.example.footballleague.ui.matchdetail

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.example.footballleague.models.DetailMatch
import com.example.footballleague.models.DetailMatchResponse
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MatchDetailViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository,
    private val app: Application
) : ViewModel() {

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

    fun getDetailMatch(eventId: String) {
        _isShowLoading.postValue(true)

        doAsync {
            val data = gson.fromJson(
                apiRepository.doRequest(
                    TheSportDbApi.getDetailMatch(eventId)
                ),
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
        doAsync {
            uiThread {
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