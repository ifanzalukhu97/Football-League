package com.example.footballleague.ui.search.screen

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.example.footballleague.R

open class SearchMatchFragmentScreen : Screen<SearchMatchFragmentScreen>() {
    val searchMatch = KView { withId(R.id.searchMatch) }
    val rvSearchMatch = KView { withId(R.id.rvSearchMatch) }
    val groupNoMatchLastMatch = KView { withId(R.id.groupNoMatchLastMatch) }

    val imgNoMatchLastMatch = KImageView { withId(R.id.imgNoMatchLastMatch) }
    val txtNoMatchLastMatch = KTextView { withId(R.id.txtNoMatchLastMatch) }

    val searchCloseBtn = KView { withId(R.id.search_close_btn) }
}