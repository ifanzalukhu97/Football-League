package com.example.footballleague.ui.favorite.teams

import android.app.Application
import androidx.lifecycle.*
import com.example.footballleague.source.local.db.database
import com.example.footballleague.source.remote.Team
import com.example.footballleague.utils.asTeam

class TeamsViewModel(
    private val app: Application
) : AndroidViewModel(app) {

    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>>
        get() = _teams

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    val isEmptyTeams: LiveData<Boolean> =
        Transformations.map(teams) { teams -> teams.isEmpty() }

    fun getTeams() {
        _isShowLoading.postValue(true)

        val favorite = app.database.getFavoriteTeam()
        _teams.postValue(favorite.asTeam())

        _isShowLoading.postValue(false)
    }

}

class TeamsViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamsViewModel::class.java)) {
            return TeamsViewModel(app) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
