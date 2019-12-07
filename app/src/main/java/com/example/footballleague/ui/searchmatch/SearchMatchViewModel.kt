package com.example.footballleague.ui.searchmatch

import androidx.lifecycle.*
import com.example.footballleague.ApiRepository
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.models.Match
import com.example.footballleague.models.SearchMatchResponse
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync

class SearchMatchViewModel(
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

    private val _matchList = MutableLiveData<List<Match>>()
    val matchList: LiveData<List<Match>>
        get() = _matchList

    val isEmptyMatchList: LiveData<Boolean> =
        Transformations.map(matchList) { matchList -> matchList.isEmpty() }

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    private fun searchMovieByQuery(query: String) {
        _isShowLoading.postValue(true)

        if (query.isBlank()) {
            _matchList.postValue(emptyList())
            _isShowLoading.postValue(false)

        } else {
            doAsync {
                val data = gson.fromJson(
                    apiRepository.doRequest(
                        TheSportDbApi.searchMatch(query)
                    ),
                    SearchMatchResponse::class.java
                )

                var sportSoccerList: List<Match> = emptyList()
                data.matchList?.let {
                    sportSoccerList = it.filter { match ->
                        match.sportType == SPORT_SOCCER_TYPE
                    }
                }

                _isShowLoading.postValue(false)
                _matchList.postValue(sportSoccerList)
            }
        }
    }

    fun submitSearchMatch(query: String) {
        // cancel last search job before start new job
        searchJob?.cancel()

        searchJob = coroutineScope.launch {
            searchMovieByQuery(query)
        }
    }

    fun queryChangeSearchMatch(query: String) {
        // cancel last search job before start new job
        searchJob?.cancel()

        searchJob = coroutineScope.launch {
            // wait 1 second after user type query
            // after that get match from searchMovieByQuery API
            delay(1000)
            searchMovieByQuery(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        searchJob?.cancel()
    }
}

class SearchMatchViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchMatchViewModel::class.java)) {
            return SearchMatchViewModel(gson, apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
