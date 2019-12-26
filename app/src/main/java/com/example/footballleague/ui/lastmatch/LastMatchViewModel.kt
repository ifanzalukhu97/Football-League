package com.example.footballleague.ui.lastmatch

import androidx.lifecycle.*
import com.example.footballleague.ApiRepository
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.source.remote.Match
import com.example.footballleague.source.remote.MatchResponse
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LastMatchViewModel(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _matchList = MutableLiveData<List<Match>>()
    val matchList: LiveData<List<Match>>
        get() = _matchList

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    val isEmptyMatchList: LiveData<Boolean> =
        Transformations.map(matchList) { matchList -> matchList.isNullOrEmpty() }

    fun getMatchList(eventId: String) {
        _isShowLoading.postValue(true)

        GlobalScope.launch {
            val data = gson.fromJson(
                apiRepository.doRequestAsync(
                    TheSportDbApi.getLastMatch(eventId)
                ).await(),
                MatchResponse::class.java
            )

            _isShowLoading.postValue(false)

            _matchList.postValue(
                when (data.matchList.isNullOrEmpty()) {
                    true -> emptyList()
                    false -> data.matchList
                }
            )
        }
    }
}

class LastMatchViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LastMatchViewModel::class.java)) {
            return LastMatchViewModel(gson, apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}