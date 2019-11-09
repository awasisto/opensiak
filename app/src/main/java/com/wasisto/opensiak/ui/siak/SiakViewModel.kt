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

package com.wasisto.opensiak.ui.siak

import androidx.lifecycle.*
import com.wasisto.opensiak.model.Account
import com.wasisto.opensiak.usecase.GetActiveAccountUseCase
import com.wasisto.opensiak.usecase.SignOutUseCase
import com.wasisto.opensiak.ui.Event
import com.wasisto.opensiak.usecase.UseCase
import timber.log.Timber
import javax.inject.Inject

class SiakViewModel @Inject constructor(
    getActiveAccountUseCase: GetActiveAccountUseCase,
    private val signOutUseCase: SignOutUseCase
): ViewModel() {

    private val getActiveAccountResult = MediatorLiveData<UseCase.Result<Account>>()

    private val signOutResult = MediatorLiveData<UseCase.Result<Unit>>()

    val activeAccountPhotoData: LiveData<ByteArray>

    val activeAccountName: LiveData<String>

    val activeAccountEmail: LiveData<String>

    val shouldShowAccountMenu: LiveData<Boolean> = MutableLiveData<Boolean>()

    val showSignOutConfirmationDialogEvent: LiveData<Event<Unit>> = MutableLiveData<Event<Unit>>()

    val launchSignInActivityEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    val finishActivityEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    val showSignOutErrorEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    val showGeneralErrorEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    init {
        getActiveAccountResult.addSource(getActiveAccountUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            getActiveAccountResult.value = result
        }

        activeAccountPhotoData = Transformations.map(getActiveAccountResult) { result ->
            (result as? UseCase.Result.Success)?.data?.photoData
        }

        activeAccountName = Transformations.map(getActiveAccountResult) { result ->
            (result as? UseCase.Result.Success)?.data?.name
        }

        activeAccountEmail = Transformations.map(getActiveAccountResult) { result ->
            (result as? UseCase.Result.Success)?.data?.email
        }

        (launchSignInActivityEvent as MediatorLiveData).addSource(signOutResult) { result ->
            if (result is UseCase.Result.Success) {
                launchSignInActivityEvent.value = Event(Unit)
            }
        }

        (finishActivityEvent as MediatorLiveData).addSource(signOutResult) { result ->
            if (result is UseCase.Result.Success) {
                finishActivityEvent.value = Event(Unit)
            }
        }

        (showSignOutErrorEvent as MediatorLiveData).addSource(signOutResult) { result ->
            if (result is UseCase.Result.Error) {
                showSignOutErrorEvent.value = Event(Unit)
            }
        }

        (showGeneralErrorEvent as MediatorLiveData).addSource(getActiveAccountResult) { result ->
            if (result is UseCase.Result.Error) {
                showGeneralErrorEvent.value = Event(Unit)
            }
        }
    }

    fun onNavigationDrawerClosed() {
        (shouldShowAccountMenu as MutableLiveData).value = false
    }

    fun onShowOrHideAccountMenuButtonClick() {
        (shouldShowAccountMenu as MutableLiveData).value = shouldShowAccountMenu.value != true
    }

    fun onSignOutButtonClick() {
        (showSignOutConfirmationDialogEvent as MutableLiveData).value = Event(Unit)
    }

    fun onSignOutConfirmationDialogYesButtonClick() {
        signOutResult.addSource(signOutUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            signOutResult.value = result
        }
    }
}