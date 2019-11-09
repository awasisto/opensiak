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

package com.wasisto.opensiak.ui.siak.classschedule

import androidx.lifecycle.*
import com.wasisto.opensiak.usecase.GetClassScheduleUseCase
import com.wasisto.opensiak.usecase.UseCase
import com.wasisto.opensiak.model.ClassSchedule
import timber.log.Timber
import javax.inject.Inject

class ClassScheduleViewModel @Inject constructor(
    private val getClassScheduleUseCase: GetClassScheduleUseCase
) : ViewModel() {

    private val getClassScheduleResult = MediatorLiveData<UseCase.Result<ClassSchedule>>()

    val isLoading: LiveData<Boolean>

    val days: LiveData<List<ClassSchedule.Day>> = MediatorLiveData<List<ClassSchedule.Day>>()

    val shouldShowSchedule: LiveData<Boolean> = MediatorLiveData<Boolean>()

    val shouldShowNoClassesIndicator: LiveData<Boolean> = MediatorLiveData<Boolean>()

    val shouldShowError: LiveData<Boolean>

    init {
        loadClassSchedule()

        isLoading = Transformations.map(getClassScheduleResult) { result ->
            result is UseCase.Result.Loading
        }

        (days as MediatorLiveData).addSource(getClassScheduleResult) { result ->
            if (result is UseCase.Result.Success) {
                days.value = result.data.days
            }
        }

        (shouldShowSchedule as MutableLiveData).value = false

        (shouldShowSchedule as MediatorLiveData).addSource(getClassScheduleResult) { result ->
            if (result is UseCase.Result.Success) {
                for (day in result.data.days) {
                    if (day.classes.isNotEmpty()) {
                        shouldShowSchedule.value = true
                        return@addSource
                    }
                }
                shouldShowSchedule.value = false
            } else if (result is UseCase.Result.Error) {
                shouldShowSchedule.value = false
            }
        }

        (shouldShowNoClassesIndicator as MutableLiveData).value = false

        (shouldShowNoClassesIndicator as MediatorLiveData).addSource(getClassScheduleResult) { result ->
            if (result is UseCase.Result.Success) {
                for (day in result.data.days) {
                    if (day.classes.isNotEmpty()) {
                        shouldShowNoClassesIndicator.value = false
                        return@addSource
                    }
                }
                shouldShowNoClassesIndicator.value = true
            } else if (result is UseCase.Result.Error) {
                shouldShowNoClassesIndicator.value = false
            }
        }

        shouldShowError = Transformations.map(getClassScheduleResult) { result ->
            result is UseCase.Result.Error
        }
    }

    fun onRefresh() {
        loadClassSchedule()
    }

    private fun loadClassSchedule() {
        getClassScheduleResult.addSource(getClassScheduleUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            getClassScheduleResult.value = result
        }
    }
}
