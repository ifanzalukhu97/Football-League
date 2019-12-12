package com.example.footballleague.ui.favorite.nextmatch

import android.app.Application
import androidx.lifecycle.*
import com.example.footballleague.source.local.db.database
import com.example.footballleague.source.remote.Match
import com.example.footballleague.utils.asListMatch

class FavoriteNextMatchViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _matchList = MutableLiveData<List<Match>>()
    val matchList: LiveData<List<Match>>
        get() = _matchList

    private val _isShowLoading = MutableLiveData<Boolean>()
    val isShowLoading: LiveData<Boolean>
        get() = _isShowLoading

    val isEmptyMatchList: LiveData<Boolean> =
        Transformations.map(matchList) { matchList -> matchList.isEmpty() }

    fun getFavoriteNextMatch() {
        _isShowLoading.postValue(true)

        val favorite = app.database.getFavoriteNextMatch()
        _matchList.postValue(favorite.asListMatch())

        _isShowLoading.postValue(false)
    }

}

class FavoriteNextMatchViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteNextMatchViewModel::class.java)) {
            return FavoriteNextMatchViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}