package com.example.footballleague.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballleague.R
import com.example.footballleague.source.remote.League
import com.example.footballleague.ui.league.LeagueItemUi
import com.example.footballleague.utils.Ids.imgBadgeLeagueItem
import com.example.footballleague.utils.Ids.txtNameLeagueItem
import kotlinx.android.extensions.LayoutContainer
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find

class LeagueRecyclerViewAdapter(
    private val leagueList: List<League>,
    private val listener: (League) -> Unit
) : RecyclerView.Adapter<LeagueRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(LeagueItemUi().createView(AnkoContext.Companion.create(parent.context, parent)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(leagueList[position], listener)
    }

    override fun getItemCount() = leagueList.size

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private val txtName = containerView.find<TextView>(txtNameLeagueItem)
        private val imgBadge = containerView.find<ImageView>(imgBadgeLeagueItem)

        fun bindItems(league: League, listener: (League) -> Unit) {
            txtName.text = league.name
            Glide.with(itemView)
                .load(league.badge)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(imgBadge)

            itemView.setOnClickListener {
                listener(league)
            }
        }
    }
}