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

package com.wasisto.opensiak.ui.about

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wasisto.opensiak.R
import com.wasisto.opensiak.ui.Event
import javax.inject.Inject

class AboutViewModel @Inject constructor() : ViewModel() {

    val launchBrowserEvent = MutableLiveData<Event<Int>>()

    val launchOssLicensesMenuActivity = MutableLiveData<Event<Unit>>()

    fun onPrivacyPolicyButtonClick() {
        launchBrowserEvent.value = Event(R.string.privacy_policy_url)
    }

    fun onOpenSourceLicensesButtonClick() {
        launchOssLicensesMenuActivity.value = Event(Unit)
    }

    fun onSourceCodeButtonClick() {
        launchBrowserEvent.value = Event(R.string.source_code_url)
    }
}