package com.example.footballleague.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballleague.R
import com.example.footballleague.source.remote.Match
import com.example.footballleague.utils.Commons
import kotlinx.android.synthetic.main.item_match.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MatchRvAdapter(
    private val isHorizontalScroll: Boolean = false,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<MatchRvAdapter.ViewHolder>() {

    private var matchList: List<Match> = emptyList()

    fun setMatchList(newMatchList: List<Match>) {
        this.matchList = newMatchList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        if (isHorizontalScroll) {
            view.layoutParams = ViewGroup.LayoutParams(
                (parent.width * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ViewHolder(view)
    }
    override fun getItemCount() = matchList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(matchList[position], listener)
    }

    class ViewHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {

        fun bindItems(match: Match, listener: (String) -> Unit) {

            with(containerView) {
                txtNameEvent.text = match.eventName
                txtDateEvent.text = Commons.convertTimeToGmtPlus7(match.dateEvent, match.timeEvent)

                val score = "${match.homeScore ?: "-"} : ${match.awayScore ?: "-"}"
                txtScore.text = score

                // If next match, hidden "FT" below score
                txtFullTimeLabel.visibility =
                    when (match.homeScore == null && match.awayScore == null) {
                        true -> View.GONE
                        false -> View.VISIBLE
                    }

                getOrCheckBadgeUrlExist(match.idHomeTeam, imgHomeTeam)
                getOrCheckBadgeUrlExist(match.idAwayTeam, imgAwayTeam)

                itemView.setOnClickListener {
                    listener(match.idEvent)
                }
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
                .override(200, 200)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(imageView)
        }
    }
}