package com.example.footballleague.ui.search.match

import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.example.footballleague.R
import com.example.footballleague.testing.SingleFragmentActivity
import com.example.footballleague.utils.EspressoIdlingResource
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchMatchFragmentTest {

    @get:Rule
    val activityRule: ActivityTestRule<SingleFragmentActivity> =
        ActivityTestRule(SingleFragmentActivity::class.java)
    private val searchMatchFragment: SearchMatchFragment = SearchMatchFragment()

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
        activityRule.activity.setFragment(searchMatchFragment)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun searchMatch() {
        onView(withId(R.id.searchMatch))
            .check(matches(isDisplayed()))
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText("man united"))

        onView(withId(R.id.rvSearchMatch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.groupNoMatchLastMatch))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun searchMatch_queryIsEmptyOrBlank() {
        onView(withId(R.id.searchMatch))
            .check(matches(isDisplayed()))
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText(""))

        onView(withId(R.id.imgNoMatchLastMatch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtNoMatchLastMatch))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchMatch_resultIsEmpty() {
        onView(withId(R.id.searchMatch))
            .check(matches(isDisplayed()))
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText("ifan zalukhu"))

        onView(withId(R.id.imgNoMatchLastMatch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtNoMatchLastMatch))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchMatch_clearQuery_matchEmpty() {
        onView(withId(R.id.searchMatch))
            .check(matches(isDisplayed()))
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText("man united"))

        onView(withId(R.id.rvSearchMatch))
            .check(matches(isDisplayed()))

        onView(withId(R.id.search_close_btn))
            .perform(ViewActions.click())

        onView(withId(R.id.imgNoMatchLastMatch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtNoMatchLastMatch))
            .check(matches(isDisplayed()))
    }
}