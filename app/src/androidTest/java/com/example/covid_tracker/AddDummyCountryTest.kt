package com.example.covid_tracker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.covid_tracker.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddDummyCountryTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isAlertDisplayed() {
        Thread.sleep(2500L)
        onView(withId(R.id.nav_saved_countries)).perform(click())
        onView(withId(R.id.btn_AddCountry)).perform(click())
        onView(withId(R.id.et_AddCountry_CountryInput)).perform(replaceText("dummy"))
        onView(withId(R.id.btn_Add)).perform(click())
        Thread.sleep(1000L)
        onView(withText(R.string.dialog_title_error)).check(matches(isDisplayed()))
        onView(withText(R.string.dialog_message_cannot_add_country)).check(matches(isDisplayed()))
    }

}