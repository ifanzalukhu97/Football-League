package com.example.footballleague.ui.matchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.adapters.MatchRvAdapter
import com.example.footballleague.ui.leaguedetail.LeagueDetailActivity.Companion.KEY_LEAGUE_ID
import com.example.footballleague.ui.matchdetail.MatchDetailActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.league_detail_match_fragment.*
import org.jetbrains.anko.support.v4.startActivity

class MatchListFragment : Fragment() {

    private var leagueId: String? = null

    companion object {
        fun newInstance(leagueId: String): MatchListFragment {
            val bundle = Bundle()
            bundle.putString(KEY_LEAGUE_ID, leagueId)

            val lastMatchFragment = MatchListFragment()
            lastMatchFragment.arguments = bundle

            return lastMatchFragment
        }
    }

    private lateinit var viewModel: LeagueDetailMatchViewModel
    private lateinit var lastMatchAdapter: MatchRvAdapter
    private lateinit var nextMatchAdapter: MatchRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.league_detail_match_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leagueId = arguments?.getString(KEY_LEAGUE_ID)

        val viewModelFactory = MatchListViewModelFactory(Gson(), ApiRepository())
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(LeagueDetailMatchViewModel::class.java)

        lastMatchAdapter =
            MatchRvAdapter(isHorizontalScroll = true) { eventId -> openMatchDetail(eventId) }
        nextMatchAdapter =
            MatchRvAdapter(isHorizontalScroll = true) { eventId -> openMatchDetail(eventId) }

        leagueId?.let {
            viewModel.getNextMatchList(it)
            viewModel.getLastMatchList(it)
        }

        populateView()
    }

    private fun populateView() {
        rvLastMatch.apply {
            adapter = lastMatchAdapter
            layoutManager = LinearLayoutManager(
                this@MatchListFragment.context, LinearLayoutManager.HORIZONTAL, false
            )
        }

        rvNextMatch.apply {
            adapter = nextMatchAdapter
            layoutManager = LinearLayoutManager(
                this@MatchListFragment.context, LinearLayoutManager.HORIZONTAL, false
            )
        }

        viewModelReadyToObserve()
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            lastMatchList.observe(viewLifecycleOwner, Observer { matchList ->
                matchList?.let { lastMatchAdapter.setMatchList(it) }
            })

            nextMatchList.observe(viewLifecycleOwner, Observer { matchList ->
                matchList?.let { nextMatchAdapter.setMatchList(it) }
            })

            isShowLoadingLastMatch.observe(viewLifecycleOwner, Observer { isDisplay ->
                progressBarLastMatch.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isShowLoadingNextMatch.observe(viewLifecycleOwner, Observer { isDisplay ->
                progressBarNextMatch.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isEmptyLastMatch.observe(viewLifecycleOwner, Observer { isEmpty ->
                groupNoMatchLastMatch.visibility = when (isEmpty) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isEmptyNextMatch.observe(viewLifecycleOwner, Observer { isEmpty ->
                groupNoMatchNextMatch.visibility = when (isEmpty) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }

    private fun openMatchDetail(eventId: String) {
        startActivity<MatchDetailActivity>(MatchDetailActivity.KEY_EVENT_ID to eventId)
    }
}
