package com.example.footballleague.ui.leaguedetail

import android.graphics.Typeface
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.core.content.ContextCompat
import com.example.footballleague.R
import com.example.footballleague.utils.Ids.containerLeagueDetail
import com.example.footballleague.utils.Ids.imgBadgeDetailLeague
import com.example.footballleague.utils.Ids.txtDescDetailLeague
import com.example.footballleague.utils.Ids.txtNameDetailLeague
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.constraint.layout.matchConstraint

class LeagueDetailActivityUi : AnkoComponent<LeagueDetailActivity> {
    override fun createView(ui: AnkoContext<LeagueDetailActivity>) = with(ui) {
        constraintLayout {
            id = containerLeagueDetail
            lparams(width = matchParent, height = matchParent)

            scrollView {
                constraintLayout {
                    lparams(width = matchParent)

                    imageView {
                        id = imgBadgeDetailLeague
                        contentDescription = resources.getString(R.string.badge_desc)
                    }.lparams(width = matchConstraint) {
                        margin = dimen(R.dimen.medium_spacing)
                        elevation = dimen(R.dimen.small_spacing).toFloat()

                        startToStart = PARENT_ID
                        endToEnd = PARENT_ID
                        topToTop = PARENT_ID
                    }

                    textView {
                        id = txtNameDetailLeague
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 18f
                        setTypeface(typeface, Typeface.BOLD)
                    }.lparams(width = matchConstraint) {
                        margin = dimen(R.dimen.normal_spacing)
                        startToStart = PARENT_ID
                        endToEnd = PARENT_ID
                        topToBottom = imgBadgeDetailLeague

                    }
                    textView {
                        id = txtDescDetailLeague
                        textColor = ContextCompat.getColor(context, R.color.colorPrimaryInverted)

                    }.lparams(width = matchConstraint) {
                        topMargin = dimen(R.dimen.normal_spacing)
                        bottomMargin = dimen(R.dimen.normal_spacing)

                        startToStart = txtNameDetailLeague
                        endToEnd = txtNameDetailLeague
                        topToBottom = txtNameDetailLeague
                        bottomToBottom = PARENT_ID
                    }
                }

            }.lparams(width = matchConstraint) {
                startToStart = PARENT_ID
                endToEnd = PARENT_ID
                topToTop = PARENT_ID
            }
        }
    }
}