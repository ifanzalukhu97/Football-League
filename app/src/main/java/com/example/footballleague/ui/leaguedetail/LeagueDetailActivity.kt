package com.example.footballleague.ui.leaguedetail

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.footballleague.R
import com.example.footballleague.models.League
import com.example.footballleague.utils.Ids.containerLeagueDetail
import com.example.footballleague.utils.Ids.imgBadgeDetailLeague
import com.example.footballleague.utils.Ids.txtDescDetailLeague
import com.example.footballleague.utils.Ids.txtNameDetailLeague
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class LeagueDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_LEAGUE = "com.example.footballleague_key_league"
    }

    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = LeagueDetailActivityUi().setContentView(this)

        val league = intent.getParcelableExtra<League>(KEY_LEAGUE)
        league?.let {
            populateView(it)
        }
    }

    private fun populateView(league: League) {

        with(league) {
            // set league badge and also extract color palette from that badge
            Glide.with(this@LeagueDetailActivity)
                .asBitmap()
                .load(badge)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        // set league badge
                        view.find<ImageView>(imgBadgeDetailLeague)
                            .setImageBitmap(resource)
                        // extract color palette
                        setViewColor(createPaletteSync(resource))
                    }
                })


            view.find<TextView>(txtNameDetailLeague).text = name
            view.find<TextView>(txtDescDetailLeague).text = description
        }
    }

    // set league detail color based on extracted color palette
    private fun setViewColor(palette: Palette?) {
        val colorPrimaryLighter = ContextCompat.getColor(this, R.color.colorPrimaryLighter)

        palette?.let {
            view.find<ConstraintLayout>(containerLeagueDetail)
                .setBackgroundColor(it.darkVibrantSwatch?.rgb ?: colorPrimaryLighter)
            view.find<TextView>(txtNameDetailLeague)
                .setTextColor(it.lightVibrantSwatch?.rgb ?: Color.WHITE)
        }
    }

    fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()
}
