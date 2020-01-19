package com.example.footballleague.ui.search.team

import androidx.lifecycle.*
import com.example.footballleague.ApiRepository
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.source.remote.SearchTeamResponse
import com.example.footballleague.source.remote.Team
import com.google.gson.Gson
import kotlinx.coroutines.*

class SearchTeamViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModel() {

    companion object {
        private const val SPORT_SOCCER_TYPE = "Soccer"
    }

    // Coroutine to handler process in background thread
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var searchJob: Job? = null

    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>>
        get() = _teams

    val isEmptyTeams: LiveData<Boolean> =
        Transformations.map(teams) { teams -> teams.isEmpty() }

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading


    fun searchTeamByQuery(query: String) {
        _isShowLoading.postValue(true)

        if (query.isBlank()) {
            _teams.postValue(emptyList())
            _isShowLoading.postValue(false)

        } else {
            GlobalScope.launch(Dispatchers.IO) {
                //                EspressoIdlingResource.increment()

                val data = gson.fromJson(
                    apiRepository.doRequestAsync(TheSportDbApi.searchTeam(query)).await(),
                    SearchTeamResponse::class.java
                )

                var sportSoccerList: List<Team> = emptyList()
                data.teams?.let {
                    sportSoccerList = it.filter { team ->
                        team.sportType == SPORT_SOCCER_TYPE
                    }
                }

                _isShowLoading.postValue(false)
                _teams.postValue(sportSoccerList)

//                if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow) {
//                    EspressoIdlingResource.decrement()
//                }
            }
        }
    }

    fun submitSearchTeam(query: String) {
        // cancel last search job before start new job
        searchJob?.cancel()

        searchJob = coroutineScope.launch {
            searchTeamByQuery(query)
        }
    }

    fun queryChangeSearchTeam(query: String) {
        // cancel last search job before start new job
        searchJob?.cancel()

        searchJob = coroutineScope.launch {
            //             EspressoIdlingResource.increment()

            // wait 1 second after user type query
            // after that get match from searchMovieByQuery API
            delay(1000)

//            if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow) {
//                EspressoIdlingResource.decrement()
//            }

            searchTeamByQuery(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        searchJob?.cancel()
    }

}

class SearchTeamViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchTeamViewModel::class.java)) {
            return SearchTeamViewModel(gson, apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}