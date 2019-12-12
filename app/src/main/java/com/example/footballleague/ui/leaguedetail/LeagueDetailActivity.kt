package com.example.footballleague.ui.leaguedetail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.source.remote.LeagueDetail
import com.example.footballleague.ui.lastmatch.LastMatchFragment
import com.example.footballleague.ui.nextmatch.NextMatchFragment
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_league_detail.*

class LeagueDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_LEAGUE_ID = "com.example.footballleague_key_league_id"
    }

    private lateinit var viewModel: LeagueDetailViewModel
    private var leagueId = -1
    private var selectedLeagueTab: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_league_detail)

        leagueId = intent.getIntExtra(KEY_LEAGUE_ID, -1)

        val viewModelFactory =
            LeagueDetailViewModelFactory(Gson(), ApiRepository(), this.application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LeagueDetailViewModel::class.java)

        viewModelReadyToObserve(leagueId)
    }


    private fun viewModelReadyToObserve(leagueId: Int) {
        with(viewModel) {
            getLeagueDetail(leagueId)

            leagueDetail.observe(this@LeagueDetailActivity, Observer { league ->
                populateView(league)
            })

            darkVibrantSwatch.observe(this@LeagueDetailActivity, Observer { darkVibrantSwatch ->
                setViewColor(darkVibrantSwatch)
            })

            isShowLoading.observe(this@LeagueDetailActivity, Observer { isDisplay ->
                progressBarDetailLeague.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })
        }
    }

    private fun populateView(league: LeagueDetail) {

        with(league) {
            viewModel.getImageBitmap(badgeUrl)

            Glide.with(this@LeagueDetailActivity)
                .load(trophyImgUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(imgTrophyDetailLeague)

            txtNameDetailLeague.text = name
            txtDescDetailLeague.text = description

            imgWebsite.setOnClickListener { loadWebUrlIntent(websiteUrl) }
            imgFacebook.setOnClickListener { loadWebUrlIntent(facebookUrl) }
            imgTwitter.setOnClickListener { loadWebUrlIntent(twitterUrl) }
            imgYoutube.setOnClickListener { loadWebUrlIntent(youtubeUrl) }
        }

        onLeagueTabSelected(getString(R.string.league_last_match))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    onLeagueTabSelected(it.text.toString())
                }
            }

        })
    }

    private fun onLeagueTabSelected(leagueMatch: String) {
        selectedLeagueTab = leagueMatch

        val fragment: Fragment? = when (leagueMatch) {
            getString(R.string.league_next_match) -> NextMatchFragment.newInstance(leagueId.toString())
            getString(R.string.league_last_match) -> LastMatchFragment.newInstance(leagueId.toString())
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(fragmentLeagueMatch.id, it)
                .commit()
        }
    }

    private fun loadWebUrlIntent(url: String) {
        val pageUrl: Uri = Uri.parse("https://${url}")
        val intent = Intent(Intent.ACTION_VIEW, pageUrl)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun setViewColor(darkVibrantSwatch: Int) {
        container
            .setBackgroundColor(darkVibrantSwatch)
        txtNameDetailLeague
            .setTextColor(darkVibrantSwatch)
        imgFacebook
            .setBackgroundColor(darkVibrantSwatch)
        imgWebsite
            .setBackgroundColor(darkVibrantSwatch)
        imgTwitter
            .setBackgroundColor(darkVibrantSwatch)
        imgYoutube
            .setBackgroundColor(darkVibrantSwatch)

        tabLayout.apply {
            setTabTextColors(Color.GRAY, darkVibrantSwatch)
            setSelectedTabIndicatorColor(darkVibrantSwatch)
        }
    }
}
