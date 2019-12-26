package com.example.footballleague.ui.leaguedetail

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.source.remote.LeagueDetail
import com.example.footballleague.source.remote.LeagueDetailResponse
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LeagueDetailViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository,
    private val app: Application
) : AndroidViewModel(app) {

    private val colorPrimaryLighter = app.getColor(R.color.colorPrimaryLighter)

    private val _leagueDetail = MutableLiveData<LeagueDetail>()
    val leagueDetail: LiveData<LeagueDetail>
        get() = _leagueDetail

    private val _darkVibrantSwatch = MutableLiveData<Int>()
    val darkVibrantSwatch: LiveData<Int>
        get() = _darkVibrantSwatch

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    fun getLeagueDetail(leagueId: Int) {
        GlobalScope.launch {
            _isShowLoading.postValue(true)

            val data = gson.fromJson(
                apiRepository.doRequestAsync(
                    TheSportDbApi.getLeagueDetails(leagueId.toString())
                ).await(),
                LeagueDetailResponse::class.java
            )

            _isShowLoading.postValue(false)
            _leagueDetail.postValue(data.leagues.first())
        }
    }

    // get Palette color from from bitmap image
    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            palette?.let {
                _darkVibrantSwatch.postValue(it.darkVibrantSwatch?.rgb ?: colorPrimaryLighter)
            }
        }
    }

    // get bitmap from imageUrl
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

class LeagueDetailViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository,
    private val app: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeagueDetailViewModel::class.java)) {
            return LeagueDetailViewModel(gson, apiRepository, app) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}