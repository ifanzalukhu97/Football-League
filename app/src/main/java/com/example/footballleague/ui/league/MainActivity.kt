package com.example.footballleague.ui.league

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.footballleague.R
import com.example.footballleague.ui.favorite.FavoriteActivity
import com.example.footballleague.ui.searchmatch.SearchMatchActivity
import com.example.footballleague.utils.Ids
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = MainActivityUi().setContentView(this)

        val toolbar = view.find<Toolbar>(Ids.toolbarLeague)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search_match -> {
                startActivity<SearchMatchActivity>()
                true
            }
            R.id.action_list_favorite -> {
                startActivity<FavoriteActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
