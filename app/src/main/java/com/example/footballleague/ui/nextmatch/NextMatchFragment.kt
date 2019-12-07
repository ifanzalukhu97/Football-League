package com.example.footballleague.ui.nextmatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.adapters.MatchRvAdapter
import com.example.footballleague.ui.matchdetail.MatchDetailActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.next_match_fragment.*
import org.jetbrains.anko.support.v4.startActivity

class NextMatchFragment : Fragment() {

    private var leagueId: String? = null

    companion object {
        private const val LEAGUE_ID_KEY = "league_id_key"

        fun newInstance(eventId: String): NextMatchFragment {
            val bundle = Bundle()
            bundle.putString(LEAGUE_ID_KEY, eventId)

            val lastMatchFragment = NextMatchFragment()
            lastMatchFragment.arguments = bundle

            return lastMatchFragment
        }
    }

    private lateinit var viewModel: NextMatchViewModel
    private lateinit var adapter: MatchRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leagueId = arguments?.getString(LEAGUE_ID_KEY)

        val viewModelFactory = NextMatchViewModelFactory(Gson(), ApiRepository())
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NextMatchViewModel::class.java)

        adapter = MatchRvAdapter { eventId ->
            startActivity<MatchDetailActivity>(MatchDetailActivity.KEY_EVENT_ID to eventId)
        }

        populateView()
    }

    private fun populateView() {
        rvNextMatch.adapter = adapter

        viewModelReadyToObserve()
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            leagueId?.let { getMatchList(it) }

            matchList.observe(this@NextMatchFragment, Observer { matchList ->
                matchList?.let { adapter.setMatchList(it) }
            })


            isShowLoading.observe(this@NextMatchFragment, Observer { isDisplay ->
                progressBarNextMatch.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isEmptyMatchList.observe(this@NextMatchFragment, Observer { isEmpty ->
                groupNoMatch.visibility = when (isEmpty) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }


    }
}
