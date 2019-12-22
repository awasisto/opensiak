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

package com.wasisto.opensiak.ui.signin

import androidx.lifecycle.*
import com.wasisto.opensiak.exception.AccountAlreadyExistsException
import com.wasisto.opensiak.exception.AuthenticationFailedException
import com.wasisto.opensiak.model.Credentials
import com.wasisto.opensiak.usecase.SignInUseCase
import com.wasisto.opensiak.usecase.UseCase
import com.wasisto.opensiak.ui.Event
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val signInUseCase: SignInUseCase) : ViewModel() {

    private val signInResult = MediatorLiveData<UseCase.Result<Unit>>()

    val username = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val isLoading: LiveData<Boolean>

    val shouldDisableForm: LiveData<Boolean>

    val launchSiakActivityEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    val finishActivityEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    val showWrongUsernameOrPasswordEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    val showAccountAlreadyExistsEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    val showGeneralSignInErrorEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    val launchAboutActivityEvent: LiveData<Event<Unit>> = MediatorLiveData<Event<Unit>>()

    init {
        isLoading = Transformations.map(signInResult) { result ->
            result is UseCase.Result.Loading
        }

        shouldDisableForm = Transformations.map(signInResult) { result ->
            result is UseCase.Result.Loading || result is UseCase.Result.Success
        }

        (launchSiakActivityEvent as MediatorLiveData).addSource(signInResult) { result ->
            if (result is UseCase.Result.Success) {
                launchSiakActivityEvent.value = Event(Unit)
            }
        }

        (finishActivityEvent as MediatorLiveData).addSource(signInResult) { result ->
            if (result is UseCase.Result.Success) {
                finishActivityEvent.value = Event(Unit)
            }
        }

        (showWrongUsernameOrPasswordEvent as MediatorLiveData).addSource(signInResult) { result ->
            if ((result as? UseCase.Result.Error)?.error is AuthenticationFailedException) {
                showWrongUsernameOrPasswordEvent.value = Event(Unit)
            }
        }

        (showAccountAlreadyExistsEvent as MediatorLiveData).addSource(signInResult) { result ->
            if ((result as? UseCase.Result.Error)?.error is AccountAlreadyExistsException) {
                showAccountAlreadyExistsEvent.value = Event(Unit)
            }
        }

        (showGeneralSignInErrorEvent as MediatorLiveData).addSource(signInResult) { result ->
            val error = (result as? UseCase.Result.Error)?.error
            if (error != null && error !is AuthenticationFailedException && error !is AccountAlreadyExistsException) {
                showGeneralSignInErrorEvent.value = Event(Unit)
            }
        }
    }

    fun onSignInButtonClick() {
        signIn()
    }

    fun onRetrySignInButtonClick() {
        signIn()
    }

    fun onAboutButtonClick() {
        (launchAboutActivityEvent as MediatorLiveData).value = Event(Unit)
    }

    private fun signIn() {
        val credentials = Credentials(username.value.orEmpty().toLowerCase(Locale.getDefault()), password.value.orEmpty())
        signInResult.addSource(signInUseCase.executeAsync(credentials)) { result ->
            if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            signInResult.value = result
        }
    }
}
