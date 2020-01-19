package com.example.footballleague.ui.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.footballleague.R
import com.example.footballleague.ui.favorite.lastmatch.FavoriteLastMatchFragment
import com.example.footballleague.ui.favorite.nextmatch.FavoriteNextMatchFragment
import com.example.footballleague.ui.favorite.teams.FavoriteTeamsFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        populateView()
    }

    private fun populateView() {
        onTabSelected(getString(R.string.favorite_last_match))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    onTabSelected(it.text.toString())
                }
            }
        })
    }

    private fun onTabSelected(tab: String) {
        val fragment: Fragment? = when (tab) {
            getString(R.string.favorite_next_match) -> FavoriteNextMatchFragment.newInstance()
            getString(R.string.favorite_last_match) -> FavoriteLastMatchFragment.newInstance()
            getString(R.string.teams) -> FavoriteTeamsFragment.newInstance()
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(fragmentFavorite.id, it)
                .commit()
        }
    }
}
