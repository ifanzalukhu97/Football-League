package com.example.footballleague.ui.teams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.footballleague.ApiRepository
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.source.remote.Team
import com.example.footballleague.source.remote.TeamsResponse
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamsViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>>
        get() = _teams

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    fun getTeams(leagueId: String) {
        GlobalScope.launch {
            _isShowLoading.postValue(true)

            val data = gson.fromJson(
                apiRepository.doRequestAsync(
                    TheSportDbApi.getAllTeams(leagueId = leagueId)
                ).await(),
                TeamsResponse::class.java
            )

            _isShowLoading.postValue(false)
            _teams.postValue(data.teams)
        }
    }

}

class TeamsViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamsViewModel::class.java)) {
            return TeamsViewModel(gson, apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
