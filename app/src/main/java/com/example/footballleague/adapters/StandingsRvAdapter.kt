package com.example.footballleague.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballleague.R
import com.example.footballleague.source.remote.Standings
import com.example.footballleague.utils.Commons
import kotlinx.android.synthetic.main.item_standings.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class StandingsRvAdapter
    : RecyclerView.Adapter<StandingsRvAdapter.ViewHolder>() {

    private var standingsList: List<Standings> = emptyList()

    fun setStandingsList(newStandingsList: List<Standings>) {
        this.standingsList = newStandingsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_standings, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = standingsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(standingsList[position], position + 1)
    }

    class ViewHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {
        fun bindItems(standings: Standings, position: Int) {
            with(containerView) {
                txtPosition.text = "$position"
                txtClub.text = standings.teamName
                txtPlayed.text = "${standings.played}"
                txtWon.text = "${standings.won}"
                txtDrawn.text = "${standings.draw}"
                txtLost.text = "${standings.loss}"
                txtGoalsDiff.text = "${standings.goalsDifference}"
                txtPoints.text = "${standings.points}"
                getOrCheckBadgeUrlExist(standings.teamId, imgClub)
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
                    uiThread {
                        setOnlineImage(badgeUrl, imageView)
                    }
                }
            }
        }

        private fun setOnlineImage(imgUrl: String, imageView: ImageView) {
            Glide.with(itemView.context.applicationContext)
                .load(imgUrl)
                .override(80, 80)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(imageView)
        }
    }

}