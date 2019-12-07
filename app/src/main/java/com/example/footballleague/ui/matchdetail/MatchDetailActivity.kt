package com.example.footballleague.ui.matchdetail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.models.DetailMatch
import com.example.footballleague.ui.searchmatch.SearchMatchActivity
import com.example.footballleague.utils.Commons
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_match_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class MatchDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_EVENT_ID = "com.example.footballleague_key_event_id"
    }

    private var eventId: String = "-1"
    private lateinit var viewModel: MatchDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        eventId = intent.getStringExtra(KEY_EVENT_ID) ?: "-1"

        val viewModelFactory =
            MatchDetailViewModelFactory(Gson(), ApiRepository(), this.application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MatchDetailViewModel::class.java)

        viewModelReadyToObserve()

        cardSearchMatch.setOnClickListener {
            startActivity<SearchMatchActivity>()
            finish()
        }
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            getDetailMatch(eventId)

            darkVibrantSwatch.observe(this@MatchDetailActivity, Observer { color ->
                setBackgroundView(color)
            })

            detailMatch.observe(this@MatchDetailActivity, Observer { match ->
                getColorPalette(match.idHomeTeam)

                with(match) {
                    setScoreView(homeScore, awayScore)
                    setGoalsView(homeGoalDetails, awayGoalDetails)

                    txtNameEvent.text = eventName
                    txtDateEvent.text = Commons.convertTimeToGmtPlus7(dateEvent, timeEvent)

                    setStatisticView(this)

                    getOrCheckBadgeUrlExist(idHomeTeam, imgHomeTeam)
                    getOrCheckBadgeUrlExist(idAwayTeam, imgAwayTeam)
                }
            })

            isShowLoading.observe(this@MatchDetailActivity, Observer { isDisplay ->
                progressBarMatchDetail.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }

    private fun setScoreView(homeScore: String?, awayScore: String?) {
        val score = "${homeScore ?: "-"} : ${awayScore ?: "-"}"
        txtScore.text = score

        // If next match, hidden "FT" below score
        txtFullTimeLabel.visibility =
            when (homeScore == null && awayScore == null) {
                true -> View.GONE
                false -> View.VISIBLE
            }
    }

    private fun setGoalsView(homeGoals: String?, awayGoals: String?) {

        // Hide section Goals Details if score is 0 : 0
        if (homeGoals.isNullOrBlank() && awayGoals.isNullOrEmpty()) {
            cardGoalDetailsMatch.visibility = View.VISIBLE

        } else {
            val homeGoalsText = homeGoals?.replace(";", "\n")
            val awayGoalsText = awayGoals?.replace(";", "\n")

            txtHomeGoalDetails.text = homeGoalsText
            txtAwayGoalDetails.text = awayGoalsText
        }
    }

    private fun setStatisticView(match: DetailMatch) {
        with(match) {
            val homeShotsString = homeShots ?: 0
            val awayShotsString = awayShots ?: 0
            txtHomeShots.text = homeShotsString.toString()
            txtAwayShots.text = awayShotsString.toString()

            val homeYellowCardsSize = (homeYellowCards?.split(";")?.size)?.minus(1)
            val awayYellowCardsSize = (awayYellowCards?.split(";")?.size)?.minus(1)
            val homeYellowCardsString = homeYellowCardsSize ?: 0
            val awayYellowCardsString = awayYellowCardsSize ?: 0
            txtHomeYellowCards.text = homeYellowCardsString.toString()
            txtAwayYellowCards.text = awayYellowCardsString.toString()

            val homeRedCardsString = (homeRedCards?.split(";")?.size)?.minus(1) ?: 0
            val awayRedCardsString = (awayRedCards?.split(";")?.size)?.minus(1) ?: 0
            txtHomeRedCards.text = homeRedCardsString.toString()
            txtAwayRedCards.text = awayRedCardsString.toString()
        }
    }

    // Check badge url from this team does loaded before
    // otherwise, get it from Team Details
    private fun getOrCheckBadgeUrlExist(teamId: String, imageView: ImageView) {
        val homeTeamBadgeUrl = Commons.listTeamBadge.find { teamBadge ->
            teamBadge.teamId == teamId
        }

        if (homeTeamBadgeUrl != null) {
            setOnlineImage(homeTeamBadgeUrl.teamBadgeUrl, imageView)

        } else {
            doAsync {
                val badgeUrl: String = Commons.getTeamBadgeUrl(teamId)
                uiThread { setOnlineImage(badgeUrl, imageView) }
            }
        }
    }

    private fun setOnlineImage(imgUrl: String, imageView: ImageView) {
        Glide.with(this)
            .load(imgUrl)
            .override(200, 200)
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_image_error)
            .into(imageView)
    }

    private fun getColorPalette(teamId: String) {
        val homeTeamBadgeUrl = Commons.listTeamBadge.find { teamBadge ->
            teamBadge.teamId == teamId
        }
        homeTeamBadgeUrl?.let {
            viewModel.getImageBitmap(it.teamBadgeUrl)
        }
    }

    private fun setBackgroundView(color: Int) {
        container.setBackgroundColor(color)
    }
}
