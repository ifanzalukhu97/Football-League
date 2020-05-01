package com.example.footballleague.ui.favorite.lastmatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.footballleague.R
import com.example.footballleague.adapters.MatchRvAdapter
import com.example.footballleague.ui.matchdetail.MatchDetailActivity
import kotlinx.android.synthetic.main.last_match_fragment.*
import org.jetbrains.anko.support.v4.startActivity

class FavoriteLastMatchFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteLastMatchFragment()
    }

    private lateinit var viewModel: FavoriteLastMatchViewModel
    private lateinit var adapter: MatchRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.last_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            val viewModelFactory = FavoriteLastMatchViewModelFactory(it.application)
            viewModel = ViewModelProvider(this, viewModelFactory)
                .get(FavoriteLastMatchViewModel::class.java)

            viewModelReadyToObserve()
        }

        adapter = MatchRvAdapter { eventId ->
            startActivity<MatchDetailActivity>(MatchDetailActivity.KEY_EVENT_ID to eventId)
        }
        rvLastMatch.adapter = adapter
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            getFavoriteLastMatch()

            matchList.observe(viewLifecycleOwner, Observer { matchList ->
                adapter.setMatchList(matchList)
            })

            isShowLoading.observe(viewLifecycleOwner, Observer { isDisplay ->
                progressBarLastMatch.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            isEmptyMatchList.observe(viewLifecycleOwner, Observer { isEmpty ->
                groupNoMatch.visibility = when (isEmpty) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteLastMatch()
    }
}
