package com.example.footballleague.ui.searchmatch

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.adapters.MatchRvAdapter
import com.example.footballleague.ui.matchdetail.MatchDetailActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search_match.*
import org.jetbrains.anko.startActivity

class SearchMatchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchMatchViewModel
    private lateinit var adapter: MatchRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_match)

        val viewModelFactory = SearchMatchViewModelFactory(Gson(), ApiRepository())
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SearchMatchViewModel::class.java)

        adapter = MatchRvAdapter { eventId ->
            startActivity<MatchDetailActivity>(MatchDetailActivity.KEY_EVENT_ID to eventId)
        }

        populateView()
    }

    private fun populateView() {
        rvSearchMatch.adapter = adapter

        onSearchMatch()

        viewModelReadyToObserve()
    }

    private fun onSearchMatch() {
        searchMatch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.submitSearchMatch(it)
                }
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                viewModel.queryChangeSearchMatch(newQuery ?: "")
                return false
            }

        })
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            matchList.observe(this@SearchMatchActivity, Observer { matchList ->
                adapter.setMatchList(matchList)
            })

            isShowLoading.observe(this@SearchMatchActivity, Observer { isDisplay ->
                progressBarSearchMatch.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isEmptyMatchList.observe(this@SearchMatchActivity, Observer { isEmpty ->
                groupNoMatch.visibility = when (isEmpty) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }
}
