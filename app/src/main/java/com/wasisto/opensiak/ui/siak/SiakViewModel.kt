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
import com.wasisto.opensiak.domain.GetActiveAccountUseCase
import com.wasisto.opensiak.domain.UseCase
import com.wasisto.opensiak.domain.UseCase.Result
import com.wasisto.opensiak.ui.Event
import timber.log.Timber
import javax.inject.Inject

class SiakViewModel @Inject constructor(getActiveAccountUseCase: GetActiveAccountUseCase): ViewModel() {

    private val getActiveAccountResult = MediatorLiveData<Result<Account>>()

    val activeAccountPhotoData: LiveData<ByteArray>

    val activeAccountName: LiveData<String>

    val activeAccountEmail: LiveData<String>

    val showErrorEvent = MediatorLiveData<Event<Unit>>()

    init {
        getActiveAccountResult.addSource(getActiveAccountUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Success) {
                Timber.d("result.data: %s", result.data)
            } else if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            getActiveAccountResult.value = result
        }

        activeAccountPhotoData = Transformations.map(getActiveAccountResult) { result ->
            (result as? Result.Success)?.data?.photoData
        }

        activeAccountName = Transformations.map(getActiveAccountResult) { result ->
            (result as? Result.Success)?.data?.name
        }

        activeAccountEmail = Transformations.map(getActiveAccountResult) { result ->
            (result as? Result.Success)?.data?.email
        }

        showErrorEvent.addSource(getActiveAccountResult) { result ->
            if (result is UseCase.Result.Error) {
                showErrorEvent.value = Event(Unit)
            }
        }
    }
}