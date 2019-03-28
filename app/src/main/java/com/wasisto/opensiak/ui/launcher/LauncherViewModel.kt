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

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.wasisto.opensiak.domain.CheckUserHasSignedInUseCase
import com.wasisto.opensiak.domain.UseCase
import com.wasisto.opensiak.ui.Event
import timber.log.Timber
import javax.inject.Inject

class LauncherViewModel @Inject constructor(checkUserHasSignedInUseCase: CheckUserHasSignedInUseCase) : ViewModel() {

    private val checkUserHasSignedInResult = MediatorLiveData<UseCase.Result<Boolean>>()

    val launchSiakActivityEvent = MediatorLiveData<Event<Unit>>()

    val launchSignInActivityEvent = MediatorLiveData<Event<Unit>>()

    val finishActivityEvent = MediatorLiveData<Event<Unit>>()

    val showErrorEvent = MediatorLiveData<Event<Unit>>()

    init {
        checkUserHasSignedInResult.addSource(checkUserHasSignedInUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Success) {
                Timber.d("result.data: %s", result.data)
            } else if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            checkUserHasSignedInResult.value = result
        }

        launchSiakActivityEvent.addSource(checkUserHasSignedInResult) { result ->
            if ((result as? UseCase.Result.Success)?.data == true) {
                launchSiakActivityEvent.value = Event(Unit)
            }
        }

        launchSignInActivityEvent.addSource(checkUserHasSignedInResult) { result ->
            if ((result as? UseCase.Result.Success)?.data == false) {
                launchSignInActivityEvent.value = Event(Unit)
            }
        }

        finishActivityEvent.addSource(checkUserHasSignedInResult) { result ->
            if (result is UseCase.Result.Success) {
                finishActivityEvent.value = Event(Unit)
            }
        }

        showErrorEvent.addSource(checkUserHasSignedInResult) { result ->
            if (result is UseCase.Result.Error) {
                showErrorEvent.value = Event(Unit)
            }
        }
    }
}