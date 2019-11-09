package com.example.footballleague.ui.league

import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballleague.R
import com.example.footballleague.adapters.LeagueRecyclerViewAdapter
import com.example.footballleague.models.Data
import com.example.footballleague.ui.leaguedetail.LeagueDetailActivity
import com.example.footballleague.utils.Ids.toolbarLeague
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.constraint.layout.matchConstraint
import org.jetbrains.anko.recyclerview.v7.recyclerView

class MainActivityUi : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

        constraintLayout {
            backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
            lparams(width = matchParent, height = matchParent)

            val toolbar = toolbar {
                id = toolbarLeague
                backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
                setTitleTextColor(ContextCompat.getColor(context, R.color.colorSecondary))
                title = resources.getString(R.string.title_league_info)

            }.lparams(width = matchParent, height = dimenAttr(R.attr.actionBarSize)) {
                elevation = dimen(R.dimen.tiny_spacing).toFloat()

                startToStart = PARENT_ID
                endToEnd = PARENT_ID
                topToTop = PARENT_ID
            }

            recyclerView {
                val leagueInfo = Data.getLeagueInfo()

                adapter = LeagueRecyclerViewAdapter(leagueList = leagueInfo) { league ->
                    startActivity<LeagueDetailActivity>(
                        LeagueDetailActivity.KEY_LEAGUE to league
                    )
                }

                layoutManager = GridLayoutManager(
                    context,
                    2,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }.lparams(width = matchConstraint, height = matchConstraint) {
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                bottomToBottom = PARENT_ID
                topToBottom = toolbar.id
            }
        }
    }
}