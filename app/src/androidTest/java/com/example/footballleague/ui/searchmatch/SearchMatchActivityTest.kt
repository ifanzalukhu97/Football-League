package com.example.footballleague.ui.searchmatch

import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.footballleague.R
import com.example.footballleague.utils.EspressoIdlingResource
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchMatchActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SearchMatchActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun searchMatch() {
        onView(withId(R.id.searchMatch))
            .check(matches(isDisplayed()))
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText("man united"))

        onView(withId(R.id.rvSearchMatch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.groupNoMatch))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun searchMatch_queryIsEmptyOrBlank() {
        onView(withId(R.id.searchMatch))
            .check(matches(isDisplayed()))
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText(""))

        onView(withId(R.id.imgNoMatch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtNoMatch))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchMatch_resultIsEmpty() {
        onView(withId(R.id.searchMatch))
            .check(matches(isDisplayed()))
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText("ifan zalukhu"))

        onView(withId(R.id.imgNoMatch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtNoMatch))
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
            .perform(click())

        onView(withId(R.id.imgNoMatch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtNoMatch))
            .check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }
}