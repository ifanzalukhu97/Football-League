package com.example.footballleague.ui.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.adapters.StandingsRvAdapter
import com.example.footballleague.ui.leaguedetail.LeagueDetailActivity.Companion.KEY_LEAGUE_ID
import com.google.gson.Gson
import kotlinx.android.synthetic.main.standings_fragment.*

class StandingsFragment : Fragment() {

    companion object {
        fun newInstance(leagueId: String): StandingsFragment {
            val bundle = Bundle()
            bundle.putString(KEY_LEAGUE_ID, leagueId)

            val standingFragment = StandingsFragment()
            standingFragment.arguments = bundle

            return standingFragment
        }
    }

    private lateinit var viewModel: StandingsViewModel
    private lateinit var adapter: StandingsRvAdapter
    private var leagueId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.standings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StandingsRvAdapter()
        rvStandings.adapter = adapter

        val viewModelFactory = StandingsViewModelFactory(Gson(), ApiRepository())
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(StandingsViewModel::class.java)

        leagueId = arguments?.getString(KEY_LEAGUE_ID)
        leagueId?.let {
            viewModel.getStandings(it)
        }

        viewModelReadyToObserve()
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            standingList.observe(viewLifecycleOwner, Observer { standingsList ->
                standingHeader.visibility = View.VISIBLE
                adapter.setStandingsList(standingsList)
            })

            isShowLoading.observe(viewLifecycleOwner, Observer { isDisplay ->
                progressBarStandings.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }
}
