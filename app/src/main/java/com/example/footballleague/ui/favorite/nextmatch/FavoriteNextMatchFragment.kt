package com.example.footballleague.ui.favorite.nextmatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.footballleague.R
import com.example.footballleague.adapters.MatchRvAdapter
import com.example.footballleague.ui.matchdetail.MatchDetailActivity
import kotlinx.android.synthetic.main.next_match_fragment.*
import org.jetbrains.anko.support.v4.startActivity

class FavoriteNextMatchFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteNextMatchFragment()
    }

    private lateinit var viewModel: FavoriteNextMatchViewModel
    private lateinit var adapter: MatchRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            val viewModelFactory = FavoriteNextMatchViewModelFactory(it.application)
            viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(FavoriteNextMatchViewModel::class.java)

            viewModelReadyToObserve()
        }

        adapter = MatchRvAdapter { eventId ->
            startActivity<MatchDetailActivity>(MatchDetailActivity.KEY_EVENT_ID to eventId)
        }
        rvNextMatch.adapter = adapter
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            getFavoriteNextMatch()

            matchList.observe(this@FavoriteNextMatchFragment, Observer { matchList ->
                adapter.setMatchList(matchList)
            })

            isShowLoading.observe(this@FavoriteNextMatchFragment, Observer { isDisplay ->
                progressBarNextMatch.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isEmptyMatchList.observe(this@FavoriteNextMatchFragment, Observer { isEmpty ->
                groupNoMatchLastMatch.visibility = when (isEmpty) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteNextMatch()
    }
}
