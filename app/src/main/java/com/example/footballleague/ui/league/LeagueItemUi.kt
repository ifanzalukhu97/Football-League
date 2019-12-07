package com.example.footballleague.ui.league

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet.MATCH_CONSTRAINT
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.core.content.ContextCompat
import com.example.footballleague.R
import com.example.footballleague.utils.Ids.imgBadgeLeagueItem
import com.example.footballleague.utils.Ids.txtNameLeagueItem
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.constraint.layout.constraintLayout


class LeagueItemUi : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        cardView {
            lparams(width = matchParent) {
                backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryLighter)
                margin = dimen(R.dimen.small_spacing)
            }

            constraintLayout {
                val imgBadge = imageView {
                    id = imgBadgeLeagueItem
                    contentDescription = resources.getString(R.string.badge_desc)
                }.lparams(width = MATCH_CONSTRAINT, height = MATCH_CONSTRAINT) {
                    startToStart = PARENT_ID
                    endToEnd = PARENT_ID
                    topToTop = PARENT_ID
                    padding = dimen(R.dimen.normal_spacing)
                    dimensionRatio = "W,4:6"
                }

                textView {
                    id = txtNameLeagueItem
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textColor = ContextCompat.getColor(context, R.color.colorSecondary)
                }.lparams(width = matchParent) {
                    topMargin = dimen(R.dimen.normal_spacing)
                    startToStart = imgBadge.id
                    endToEnd = imgBadge.id
                    topToBottom = imgBadge.id
                    bottomToBottom = PARENT_ID
                }


            }.lparams(width = matchParent) {
                padding = dimen(R.dimen.normal_spacing)
            }
        }
    }
}