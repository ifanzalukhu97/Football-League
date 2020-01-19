package com.example.footballleague.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballleague.R
import com.example.footballleague.source.remote.Team
import kotlinx.android.synthetic.main.item_team.view.*

class TeamsRvAdapter(
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<TeamsRvAdapter.ViewHolder>() {

    private var teams: List<Team> = emptyList()

    fun setTeams(newTeams: List<Team>) {
        this.teams = newTeams
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(teams[position], listener)
    }

    class ViewHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {
        fun bindItems(team: Team, listener: (String) -> Unit) {
            with(containerView) {
                txtNameTeam.text = team.teamName
                txtDescTeam.text = team.teamDesc

                Glide.with(itemView.context.applicationContext)
                    .load(team.teamBadge)
                    .override(200, 200)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(imgBadgeTeam)

                itemView.setOnClickListener {
                    listener(team.teamId)
                }
            }
        }
    }
}