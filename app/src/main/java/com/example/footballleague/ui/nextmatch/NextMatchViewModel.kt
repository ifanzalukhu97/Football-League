package com.example.footballleague.ui.nextmatch

import androidx.lifecycle.*
import com.example.footballleague.ApiRepository
import com.example.footballleague.TheSportDbApi
import com.example.footballleague.models.Match
import com.example.footballleague.models.MatchResponse
import com.google.gson.Gson
import org.jetbrains.anko.doAsync

class NextMatchViewModel(
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
        Transformations.map(matchList) { matchList -> matchList.isEmpty() }

    fun getMatchList(eventId: String) {
        _isShowLoading.postValue(true)

        doAsync {
            val data = gson.fromJson(
                apiRepository.doRequest(
                    TheSportDbApi.getNextMatch(eventId)
                ),
                MatchResponse::class.java
            )

            _matchList.postValue(
                when (data.matchList.isNullOrEmpty()) {
                    true -> emptyList()
                    false -> data.matchList
                }
            )

            _isShowLoading.postValue(false)

        }
    }
}

class NextMatchViewModelFactory(
    private val gson: Gson,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NextMatchViewModel::class.java)) {
            return NextMatchViewModel(gson, apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
