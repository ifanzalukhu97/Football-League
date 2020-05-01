package com.example.footballleague.ui.favorite.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.footballleague.R
import com.example.footballleague.adapters.TeamsRvAdapter
import com.example.footballleague.ui.teamdetail.TeamDetailActivity
import kotlinx.android.synthetic.main.team_fragment.*
import org.jetbrains.anko.support.v4.startActivity

class FavoriteTeamsFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteTeamsFragment()
    }

    private lateinit var viewModel: TeamsViewModel
    private lateinit var adapter: TeamsRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.team_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            val viewModelFactory = TeamsViewModelFactory(it.application)
            viewModel = ViewModelProvider(this, viewModelFactory)
                .get(TeamsViewModel::class.java)

            viewModelReadyToObserve()
        }

        adapter = TeamsRvAdapter { teamId ->
            startActivity<TeamDetailActivity>(TeamDetailActivity.KEY_TEAM_ID to teamId)
        }
        rvTeams.adapter = adapter
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            viewModel.getTeams()

            teams.observe(viewLifecycleOwner, Observer { teams ->
                adapter.setTeams(teams)
            })

            isShowLoading.observe(viewLifecycleOwner, Observer { isDisplay ->
                progressBarTeams.visibility = when (isDisplay) {
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

    override fun onResume() {
        super.onResume()
        viewModel.getTeams()
    }
}
