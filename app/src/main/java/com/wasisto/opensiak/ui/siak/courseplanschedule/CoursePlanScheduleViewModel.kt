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

package com.wasisto.opensiak.ui.siak.courseplanschedule

import androidx.lifecycle.*
import com.wasisto.opensiak.domain.GetCoursePlanScheduleUseCase
import com.wasisto.opensiak.domain.UseCase
import com.wasisto.opensiak.model.CoursePlanSchedule
import timber.log.Timber
import javax.inject.Inject

class CoursePlanScheduleViewModel @Inject constructor(
    private val getCoursePlanScheduleUseCase: GetCoursePlanScheduleUseCase
) : ViewModel() {

    private val getCoursePlanScheduleResult = MediatorLiveData<UseCase.Result<CoursePlanSchedule>>()

    val isLoading: LiveData<Boolean>

    val days = MediatorLiveData<List<CoursePlanSchedule.Day>>()

    val shouldShowSchedule = MediatorLiveData<Boolean>()

    val shouldShowNoClassesIndicator = MediatorLiveData<Boolean>()

    val shouldShowError: LiveData<Boolean>

    init {
        loadCoursePlanSchedule()

        isLoading = Transformations.map(getCoursePlanScheduleResult) { result ->
            result is UseCase.Result.Loading
        }

        days.addSource(getCoursePlanScheduleResult) { result ->
            if (result is UseCase.Result.Success) {
                days.value = result.data.days
            }
        }

        shouldShowSchedule.value = false

        shouldShowSchedule.addSource(getCoursePlanScheduleResult) { result ->
            if (result is UseCase.Result.Success) {
                shouldShowSchedule.value = true
            } else if (result is UseCase.Result.Error) {
                shouldShowSchedule.value = false
            }
        }

        shouldShowNoClassesIndicator.value = false

        shouldShowNoClassesIndicator.addSource(getCoursePlanScheduleResult) { result ->
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

        shouldShowError = Transformations.map(getCoursePlanScheduleResult) { result ->
            result is UseCase.Result.Error
        }
    }

    fun onRefresh() = loadCoursePlanSchedule()

    private fun loadCoursePlanSchedule() =
        getCoursePlanScheduleResult.addSource(getCoursePlanScheduleUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Success) {
                Timber.d("result.data: %s", result.data)
            } else if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            getCoursePlanScheduleResult.value = result
        }
}
