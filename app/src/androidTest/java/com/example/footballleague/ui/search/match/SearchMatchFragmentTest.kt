package com.example.footballleague.ui.search.match

import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen
import com.example.footballleague.testing.SingleFragmentActivity
import com.example.footballleague.ui.search.screen.SearchMatchFragmentScreen
import com.example.footballleague.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchMatchFragmentTest {

    @get:Rule
    val activityRule: ActivityTestRule<SingleFragmentActivity> =
        ActivityTestRule(SingleFragmentActivity::class.java)
    private val searchMatchFragment: SearchMatchFragment = SearchMatchFragment()

    private lateinit var searchScreen: Screen<SearchMatchFragmentScreen>

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
        activityRule.activity.setFragment(searchMatchFragment)

        searchScreen = SearchMatchFragmentScreen()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun searchMatch() {
        searchScreen {
            searchMatch.isDisplayed()

            onView(isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(typeText("man united"))

            rvSearchMatch.isDisplayed()
            groupNoMatchLastMatch.isNotDisplayed()
        }
    }

    @Test
    fun searchMatch_queryIsEmptyOrBlank() {
        searchScreen {
            searchMatch.isDisplayed()

            onView(isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(typeText(""))

            imgNoMatchLastMatch.isDisplayed()
            txtNoMatchLastMatch.isDisplayed()
        }
    }

    @Test
    fun searchMatch_resultIsEmpty() {
        searchScreen {
            searchMatch.isDisplayed()

            onView(isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(typeText("ifan zalukhu"))

            imgNoMatchLastMatch.isDisplayed()
            txtNoMatchLastMatch.isDisplayed()
        }
    }

    @Test
    fun searchMatch_clearQuery_matchEmpty() {
        searchScreen {
            searchMatch.isDisplayed()

            onView(isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(typeText("man united"))
            rvSearchMatch.isDisplayed()

            searchCloseBtn.perform { click() }
            imgNoMatchLastMatch.isDisplayed()
            txtNoMatchLastMatch.isDisplayed()
        }
    }
}