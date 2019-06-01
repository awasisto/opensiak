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
import com.wasisto.opensiak.domain.GetClassScheduleUseCase
import com.wasisto.opensiak.domain.UseCase
import com.wasisto.opensiak.model.ClassSchedule
import timber.log.Timber
import javax.inject.Inject

class ClassScheduleViewModel @Inject constructor(
    private val getClassScheduleUseCase: GetClassScheduleUseCase
) : ViewModel() {

    private val getClassScheduleResult = MediatorLiveData<UseCase.Result<ClassSchedule>>()

    val isLoading: LiveData<Boolean>

    val days = MediatorLiveData<List<ClassSchedule.Day>>()

    val shouldShowSchedule = MediatorLiveData<Boolean>()

    val shouldShowNoClassesIndicator = MediatorLiveData<Boolean>()

    val shouldShowError: LiveData<Boolean>

    init {
        loadClassSchedule()

        isLoading = Transformations.map(getClassScheduleResult) { result ->
            result is UseCase.Result.Loading
        }

        days.addSource(getClassScheduleResult) { result ->
            if (result is UseCase.Result.Success) {
                days.value = result.data.days
            }
        }

        shouldShowSchedule.value = false

        shouldShowSchedule.addSource(getClassScheduleResult) { result ->
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

        shouldShowNoClassesIndicator.value = false

        shouldShowNoClassesIndicator.addSource(getClassScheduleResult) { result ->
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

    fun onRefresh() = loadClassSchedule()

    private fun loadClassSchedule() =
        getClassScheduleResult.addSource(getClassScheduleUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Success) {
                Timber.d("result.data: %s", result.data)
            } else if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            getClassScheduleResult.value = result
        }
}
