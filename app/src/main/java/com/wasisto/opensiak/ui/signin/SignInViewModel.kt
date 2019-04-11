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
import com.wasisto.opensiak.R
import com.wasisto.opensiak.data.account.AccountAlreadyExistsException
import com.wasisto.opensiak.data.siakng.AuthenticationFailedException
import com.wasisto.opensiak.model.Credentials
import com.wasisto.opensiak.domain.SignInUseCase
import com.wasisto.opensiak.domain.UseCase
import com.wasisto.opensiak.ui.Event
import timber.log.Timber
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val signInUseCase: SignInUseCase) : ViewModel() {

    private val signInResult = MediatorLiveData<UseCase.Result<Unit>>()

    val username = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val isLoading: LiveData<Boolean>

    val shouldDisableForm: LiveData<Boolean>

    val launchSiakActivityEvent = MediatorLiveData<Event<Unit>>()

    val finishActivityEvent = MediatorLiveData<Event<Unit>>()

    val showSignInErrorSnackbarEvent = MediatorLiveData<Event<SignInErrorSnackbarEventData>>()

    val launchAboutActivityEvent = MediatorLiveData<Event<Unit>>()

    init {
        isLoading = Transformations.map(signInResult) { result ->
            result is UseCase.Result.Loading
        }

        shouldDisableForm = Transformations.map(signInResult) { result ->
            result is UseCase.Result.Loading || result is UseCase.Result.Success
        }

        launchSiakActivityEvent.addSource(signInResult) { result ->
            if (result is UseCase.Result.Success) {
                launchSiakActivityEvent.value = Event(Unit)
            }
        }

        finishActivityEvent.addSource(signInResult) { result ->
            if (result is UseCase.Result.Success) {
                finishActivityEvent.value = Event(Unit)
            }
        }

        showSignInErrorSnackbarEvent.addSource(signInResult) { result ->
            if (result is UseCase.Result.Error) {
                when {
                    result.error is AuthenticationFailedException ->
                        showSignInErrorSnackbarEvent.value = Event(
                            SignInErrorSnackbarEventData(
                                messageResId = R.string.wrong_username_or_password,
                                showRetryButton = false
                            )
                        )
                    result.error is AccountAlreadyExistsException ->
                        showSignInErrorSnackbarEvent.value = Event(
                            SignInErrorSnackbarEventData(
                                messageResId = R.string.account_already_exists,
                                showRetryButton = false
                            )
                        )
                    else -> {
                        Timber.w(result.error)
                        showSignInErrorSnackbarEvent.value = Event(
                            SignInErrorSnackbarEventData(
                                messageResId = R.string.something_went_wrong,
                                showRetryButton = true
                            )
                        )
                    }
                }
            }
        }
    }

    fun onSignInButtonClick() = doSignIn()

    fun onRetrySignInButtonClick() = doSignIn()

    fun onAboutButtonClick() {
        launchAboutActivityEvent.value = Event(Unit)
    }

    private fun doSignIn() {
        val credentials = Credentials(username.value.orEmpty().toLowerCase(), password.value.orEmpty())
        signInResult.addSource(signInUseCase.executeAsync(credentials)) { result ->
            if (result is UseCase.Result.Success) {
                Timber.d("result.data: %s", result.data)
            } else if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            signInResult.value = result
        }
    }

    data class SignInErrorSnackbarEventData(
        val messageResId: Int,
        val showRetryButton: Boolean
    )
}
