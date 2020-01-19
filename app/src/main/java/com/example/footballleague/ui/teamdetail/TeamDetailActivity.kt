package com.example.footballleague.ui.teamdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.footballleague.ApiRepository
import com.example.footballleague.R
import com.example.footballleague.source.remote.TeamDetail
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_team_detail.*

class TeamDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_TEAM_ID = "team_id"
    }

    private lateinit var viewModel: TeamDetailViewModel
    private var teamId: String? = null
    private var menuItem: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        setSupportActionBar(toolbar)

        teamId = intent.getStringExtra(KEY_TEAM_ID)

        val viewModelFactory = TeamDetailViewModelFactory(Gson(), ApiRepository(), this.application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(TeamDetailViewModel::class.java)

        teamId?.let {
            viewModel.getDetailTeam(it)
        }

        viewModelReadyToObserve()
    }

    private fun viewModelReadyToObserve() {
        with(viewModel) {
            favoriteState(teamId ?: "-1")
            teamDetail.observe(this@TeamDetailActivity, Observer { teamDetail ->
                populateView(teamDetail)
            })

            darkVibrantSwatch.observe(this@TeamDetailActivity, Observer { darkVibrantSwatch ->
                setViewColor(darkVibrantSwatch)
            })

            isShowLoading.observe(this@TeamDetailActivity, Observer { isDisplay ->
                progressBar.visibility = when (isDisplay) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            })

            addFavoriteResponse.observe(this@TeamDetailActivity, Observer { message ->
                Toast.makeText(this@TeamDetailActivity, message, Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun populateView(teamDetail: TeamDetail) {
        with(teamDetail) {
            txtNameTeam.text = teamName
            txtNicknameTeam.text = nicknamed
            val stadiumDesc = String.format(
                getString(R.string.stadium_value),
                stadium,
                stadiumLocation,
                teamName,
                stadiumCapacity
            )
            txtStadium.text = stadiumDesc
            txtDescTeam.text = teamDesc

            Glide.with(this@TeamDetailActivity)
                .load(teamBadge)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(imgBadgeTeam)

            viewModel.getImageBitmap(teamBadge)
        }
    }

    private fun setViewColor(darkVibrantSwatch: Int) {
        container
            .setBackgroundColor(darkVibrantSwatch)
        txtNameTeam.setTextColor(darkVibrantSwatch)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_team_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_to_favorite -> {
                with(viewModel) {
                    if (isFavorite) removeTeamFromFavorite() else addTeamToFavorite()

                    isFavorite = !isFavorite
                    setFavorite()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setFavorite() {
        menuItem?.getItem(0)?.icon = when (viewModel.isFavorite) {
            true -> ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
            false -> ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
        }
    }
}
