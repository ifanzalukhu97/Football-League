package com.example.footballleague.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.footballleague.R
import com.example.footballleague.ui.search.match.SearchMatchFragment
import com.example.footballleague.ui.search.team.SearchTeamFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        tabLayoutListener()
    }

    private fun onSearchTabSelected(searchTab: String) {
        val fragment: Fragment? = when (searchTab) {
            getString(R.string.search_match_label) -> SearchMatchFragment.newInstance()
            getString(R.string.search_team_label) -> SearchTeamFragment.newInstance()
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(fragmentSearch.id, it)
                .commit()
        }
    }

    private fun tabLayoutListener() {
        onSearchTabSelected(getString(R.string.search_match_label))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    onSearchTabSelected(it.text.toString())
                }
            }

        })
    }
}
