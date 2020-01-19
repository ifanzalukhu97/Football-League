package com.example.footballleague.ui.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.adapters.TeamsRvAdapter
import com.example.footballleague.ui.leaguedetail.LeagueDetailActivity.Companion.KEY_LEAGUE_ID
import com.example.footballleague.ui.teamdetail.TeamDetailActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.team_fragment.*
import org.jetbrains.anko.support.v4.startActivity

class TeamsFragment : Fragment() {

    companion object {
        fun newInstance(leagueId: String): TeamsFragment {
            val bundle = Bundle()
            bundle.putString(KEY_LEAGUE_ID, leagueId)

            val teamsFragment = TeamsFragment()
            teamsFragment.arguments = bundle

            return teamsFragment
        }
    }

    private lateinit var viewModel: TeamsViewModel
    private var leagueId: String? = null
    private lateinit var adapter: TeamsRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.team_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = TeamsViewModelFactory(Gson(), ApiRepository())
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(TeamsViewModel::class.java)

        adapter = TeamsRvAdapter { teamId ->
            startActivity<TeamDetailActivity>(TeamDetailActivity.KEY_TEAM_ID to teamId)
        }
        rvTeams.adapter = adapter

        leagueId = arguments?.getString(KEY_LEAGUE_ID)
        leagueId?.let {
            viewModel.getTeams(it)
        }

        viewModelReadyToObserve()
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            teams.observe(this@TeamsFragment, Observer { teams ->
                adapter.setTeams(teams)
            })

            isShowLoading.observe(this@TeamsFragment, Observer { isDisplay ->
                progressBarTeams.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }
}
