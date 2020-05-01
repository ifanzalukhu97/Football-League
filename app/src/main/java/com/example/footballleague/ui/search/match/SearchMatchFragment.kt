package com.example.footballleague.ui.search.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.adapters.MatchRvAdapter
import com.example.footballleague.ui.matchdetail.MatchDetailActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.search_match_fragment.*
import org.jetbrains.anko.support.v4.startActivity

class SearchMatchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchMatchFragment()
    }

    private lateinit var viewModel: SearchMatchViewModel
    private lateinit var adapter: MatchRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = SearchMatchViewModelFactory(Gson(), ApiRepository())
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SearchMatchViewModel::class.java)

        populateView()

        viewModelReadyToObserve()
    }

    private fun populateView() {
        adapter = MatchRvAdapter { eventId ->
            startActivity<MatchDetailActivity>(MatchDetailActivity.KEY_EVENT_ID to eventId)
        }
        rvSearchMatch.adapter = adapter

        onSearchMatch()
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
            matchList.observe(viewLifecycleOwner, Observer { matchList ->
                adapter.setMatchList(matchList)
            })

            isShowLoading.observe(viewLifecycleOwner, Observer { isDisplay ->
                progressBarSearchMatch.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isEmptyMatchList.observe(viewLifecycleOwner, Observer { isEmpty ->
                groupNoMatchLastMatch.visibility = when (isEmpty) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }
}
