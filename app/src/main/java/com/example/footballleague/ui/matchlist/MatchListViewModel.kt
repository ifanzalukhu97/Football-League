package com.example.footballleague.ui.matchlist

import androidx.lifecycle.*
import com.example.footballleague.ApiRepository
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.source.remote.Match
import com.example.footballleague.source.remote.MatchResponse
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LeagueDetailMatchViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _lastMatchList = MutableLiveData<List<Match>>()
    val lastMatchList: LiveData<List<Match>>
        get() = _lastMatchList

    private val _nextMatchList = MutableLiveData<List<Match>>()
    val nextMatchList: LiveData<List<Match>>
        get() = _nextMatchList

    private val _isShowLoadingLastMatch = MutableLiveData<Boolean>()
    val isShowLoadingLastMatch: LiveData<Boolean>
        get() = _isShowLoadingLastMatch

    private val _isShowLoadingNextMatch = MutableLiveData<Boolean>()
    val isShowLoadingNextMatch: LiveData<Boolean>
        get() = _isShowLoadingNextMatch

    val isEmptyLastMatch: LiveData<Boolean> =
        Transformations.map(lastMatchList) { lastMatchList -> lastMatchList.isNullOrEmpty() }

    val isEmptyNextMatch: LiveData<Boolean> =
        Transformations.map(nextMatchList) { nextMatchList -> nextMatchList.isNullOrEmpty() }

    fun getLastMatchList(eventId: String) {
        _isShowLoadingLastMatch.postValue(true)

        GlobalScope.launch {
            val data = gson.fromJson(
                apiRepository.doRequestAsync(
                    TheSportDbApi.getLastMatch(eventId)
                ).await(),
                MatchResponse::class.java
            )

            _isShowLoadingLastMatch.postValue(false)

            _lastMatchList.postValue(
                when (data.matchList.isNullOrEmpty()) {
                    true -> emptyList()
                    false -> data.matchList
                }
            )
        }
    }

    fun getNextMatchList(eventId: String) {
        _isShowLoadingNextMatch.postValue(true)

        GlobalScope.launch {
            val data = gson.fromJson(
                apiRepository.doRequestAsync(
                    TheSportDbApi.getNextMatch(eventId)
                ).await(),
                MatchResponse::class.java
            )

            _isShowLoadingNextMatch.postValue(false)

            _nextMatchList.postValue(
                when (data.matchList.isNullOrEmpty()) {
                    true -> emptyList()
                    false -> data.matchList
                }
            )
        }
    }
}

class MatchListViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeagueDetailMatchViewModel::class.java)) {
            return LeagueDetailMatchViewModel(gson, apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}