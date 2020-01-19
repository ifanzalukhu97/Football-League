package com.example.footballleague.ui.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.footballleague.ApiRepository
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.source.remote.Standings
import com.example.footballleague.source.remote.StandingsResponse
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StandingsViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _standingList = MutableLiveData<List<Standings>>()
    val standingList: LiveData<List<Standings>>
        get() = _standingList

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    fun getStandings(leagueId: String) {
        GlobalScope.launch {
            _isShowLoading.postValue(true)

            val data = gson.fromJson(
                apiRepository.doRequestAsync(
                    TheSportDbApi.getStandings(leagueId = leagueId, season = "19/20")
                ).await(),
                StandingsResponse::class.java
            )

            _isShowLoading.postValue(false)
            _standingList.postValue(data.standings)
        }
    }

}

class StandingsViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StandingsViewModel::class.java)) {
            return StandingsViewModel(gson, apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}