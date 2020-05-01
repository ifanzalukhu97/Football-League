package com.example.footballleague.ui.search.screen

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.screen.Screen
import com.example.footballleague.R

class SearchTeamFragmentScreen : Screen<SearchTeamFragmentScreen>() {
    val searchTeam = KView { withId(R.id.searchTeam) }
    val rvSearchTeam = KView { withId(R.id.rvSearchTeam) }
    val txtNoTeam = KView { withId(R.id.txtNoTeam) }

    val searchCloseBtn = KView { withId(R.id.search_close_btn) }
}