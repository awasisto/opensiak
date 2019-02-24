/*
 * Copyright (C) 2019 Andika Wasisto
 *
 * This file is part of OpenSIAK.
 *
 * OpenSIAK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenSIAK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenSIAK.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.wasisto.opensiak.ui.launcher

import androidx.test.espresso.intent.Intents
import com.wasisto.opensiak.ui.signin.SignInActivity
import org.junit.Rule
import org.junit.Test

import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.rule.ActivityTestRule
import com.wasisto.opensiak.ui.siak.SiakActivity
import org.hamcrest.Matchers.anyOf
import org.junit.After
import org.junit.Before

class LauncherActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(LauncherActivity::class.java, true, false)

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun launch() {
        activityTestRule.launchActivity(null)
        intended(
            anyOf(
                hasComponent(SiakActivity::class.java.name),
                hasComponent(SignInActivity::class.java.name)
            )
        )
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}
