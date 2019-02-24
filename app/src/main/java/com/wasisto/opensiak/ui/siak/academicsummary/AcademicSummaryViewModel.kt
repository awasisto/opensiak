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

package com.wasisto.opensiak.ui.siak.academicsummary

import androidx.lifecycle.*
import com.wasisto.opensiak.model.AcademicSummary
import com.wasisto.opensiak.domain.GetAcademicSummaryUseCase
import com.wasisto.opensiak.domain.UseCase
import com.wasisto.opensiak.util.LocaleUtils
import timber.log.Timber
import javax.inject.Inject

class AcademicSummaryViewModel @Inject constructor(
    private val getAcademicSummaryUseCase: GetAcademicSummaryUseCase
) : ViewModel() {

    private val getAcademicSummaryResult = MediatorLiveData<UseCase.Result<AcademicSummary>>()

    val isLoading: LiveData<Boolean>

    val studentPhotoData = MediatorLiveData<ByteArray>()

    val studentName = MediatorLiveData<String>()

    val majorAndDegree = MediatorLiveData<String>()

    val cgpa = MediatorLiveData<Float>()

    val totalCreditsEarned = MediatorLiveData<Int>()

    val gradeCounts = MediatorLiveData<List<AcademicSummary.GradeStatistics.GradeCount>>()

    val shouldShowSummary = MediatorLiveData<Boolean>()

    val shouldShowError: LiveData<Boolean>

    init {
        loadAcademicSummary()

        isLoading = Transformations.map(getAcademicSummaryResult) { result ->
            result is UseCase.Result.Loading
        }

        studentPhotoData.addSource(getAcademicSummaryResult) { result ->
            if (result is UseCase.Result.Success) {
                studentPhotoData.value = result.data.studentPhotoData
            }
        }

        studentName.addSource(getAcademicSummaryResult) { result ->
            if (result is UseCase.Result.Success) {
                studentName.value = result.data.studentName
            }
        }

        majorAndDegree.addSource(getAcademicSummaryResult) { result ->
            if (result is UseCase.Result.Success) {
                if (LocaleUtils.isDefaultLocaleLanguageIndonesian()) {
                    majorAndDegree.value = "${result.data.degreeInd} ${result.data.majorInd}"
                } else {
                    majorAndDegree.value = "${result.data.majorEng} ${result.data.degreeEng}"
                }
            }
        }

        cgpa.addSource(getAcademicSummaryResult) { result ->
            if (result is UseCase.Result.Success) {
                cgpa.value = result.data.cumulativeGpa
            }
        }

        totalCreditsEarned.addSource(getAcademicSummaryResult) { result ->
            if (result is UseCase.Result.Success) {
                totalCreditsEarned.value = result.data.totalCreditsEarned
            }
        }

        gradeCounts.addSource(getAcademicSummaryResult) { result ->
            if (result is UseCase.Result.Success) {
                gradeCounts.value = result.data.gradeStatistics.gradeCounts
            }
        }

        shouldShowError = Transformations.map(getAcademicSummaryResult) { result ->
            result is UseCase.Result.Error
        }

        shouldShowSummary.value = false

        shouldShowSummary.addSource(getAcademicSummaryResult) { result ->
            if (result is UseCase.Result.Success) {
                shouldShowSummary.value = true
            } else if (result is UseCase.Result.Error) {
                shouldShowSummary.value = false
            }
        }
    }

    fun onRefresh() = loadAcademicSummary()

    private fun loadAcademicSummary() =
        getAcademicSummaryResult.addSource(getAcademicSummaryUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Success) {
                Timber.d("result.data: %s", result.data)
            } else if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            getAcademicSummaryResult.value = result
        }
}
