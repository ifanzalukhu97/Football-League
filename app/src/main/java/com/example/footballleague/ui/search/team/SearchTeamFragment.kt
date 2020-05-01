package com.example.footballleague.ui.search.team

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
import com.example.footballleague.adapters.TeamsRvAdapter
import com.example.footballleague.ui.teamdetail.TeamDetailActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.search_team_fragment.*
import org.jetbrains.anko.support.v4.startActivity

class SearchTeamFragment : Fragment() {

    companion object {
        fun newInstance() = SearchTeamFragment()
    }

    private lateinit var viewModel: SearchTeamViewModel
    private lateinit var adapter: TeamsRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_team_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = SearchTeamViewModelFactory(Gson(), ApiRepository())
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SearchTeamViewModel::class.java)

        populateView()

        viewModelReadyToObserve()
    }

    private fun populateView() {
        adapter = TeamsRvAdapter { teamId ->
            startActivity<TeamDetailActivity>(TeamDetailActivity.KEY_TEAM_ID to teamId)
        }
        rvSearchTeam.adapter = adapter

        onSearchTeam()
    }

    private fun onSearchTeam() {
        searchTeam.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.submitSearchTeam(it)
                }
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                viewModel.queryChangeSearchTeam(newQuery ?: "")
                return false
            }

        })
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            teams.observe(viewLifecycleOwner, Observer { teams ->
                adapter.setTeams(teams)
            })

            isShowLoading.observe(viewLifecycleOwner, Observer { isDisplay ->
                progressBarSearchTeam.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isEmptyTeams.observe(viewLifecycleOwner, Observer { isEmpty ->
                txtNoTeam.visibility = when (isEmpty) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }
}
