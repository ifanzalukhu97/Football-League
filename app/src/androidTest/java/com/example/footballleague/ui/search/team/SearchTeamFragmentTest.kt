package com.example.footballleague.ui.search.team

import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen
import com.example.footballleague.testing.SingleFragmentActivity
import com.example.footballleague.ui.search.screen.SearchTeamFragmentScreen
import com.example.footballleague.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchTeamFragmentTest {

    @get:Rule
    val activityRule: ActivityTestRule<SingleFragmentActivity> =
        ActivityTestRule(SingleFragmentActivity::class.java)
    private val searchTeamFragment: SearchTeamFragment = SearchTeamFragment()

    private lateinit var searchTeamScreen: Screen<SearchTeamFragmentScreen>

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
        activityRule.activity.setFragment(searchTeamFragment)

        searchTeamScreen = SearchTeamFragmentScreen()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun searchTeam() {
        searchTeamScreen {
            searchTeam.isDisplayed()
            onView(isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(typeText("man united"))

            rvSearchTeam.isDisplayed()
            txtNoTeam.isNotDisplayed()
        }
    }

    @Test
    fun searchTeam_queryIsEmptyOrBlank() {
        searchTeamScreen {
            searchTeam.isDisplayed()
            onView(isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(typeText(""))

            txtNoTeam.isDisplayed()
        }
    }

    @Test
    fun searchTeam_resultIsEmpty() {
        searchTeamScreen {
            searchTeam.isDisplayed()
            onView(isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(typeText("ifan zalukhu"))

            txtNoTeam.isDisplayed()
        }
    }

    @Test
    fun searchTeam_clearQuery_teamEmpty() {
        searchTeamScreen {
            searchTeam.isDisplayed()
            onView(isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(typeText("man united"))

            rvSearchTeam.isDisplayed()

            searchCloseBtn.perform { click() }

            txtNoTeam.isDisplayed()
        }
    }
}