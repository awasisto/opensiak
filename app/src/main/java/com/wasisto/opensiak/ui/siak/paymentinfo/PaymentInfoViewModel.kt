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

package com.wasisto.opensiak.ui.siak.paymentinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wasisto.opensiak.domain.GetPaymentInfoUseCase
import com.wasisto.opensiak.domain.UseCase
import com.wasisto.opensiak.model.PaymentInfo
import timber.log.Timber
import javax.inject.Inject

class PaymentInfoViewModel @Inject constructor(
    private val getPaymentInfoUseCase: GetPaymentInfoUseCase
) : ViewModel() {

    private val getPaymentInfoResult = MediatorLiveData<UseCase.Result<PaymentInfo>>()

    val isLoading: LiveData<Boolean>

    val academicYears = MediatorLiveData<List<PaymentInfo.AcademicYear>>()

    val shouldShowPaymentInfoTable = MediatorLiveData<Boolean>()

    val shouldShowError: LiveData<Boolean>

    init {
        loadPaymentInfo()

        isLoading = Transformations.map(getPaymentInfoResult) { result ->
            result is UseCase.Result.Loading
        }

        academicYears.addSource(getPaymentInfoResult) { result ->
            if (result is UseCase.Result.Success) {
                academicYears.value = result.data.academicYears
            }
        }

        shouldShowPaymentInfoTable.value = false

        shouldShowPaymentInfoTable.addSource(getPaymentInfoResult) { result ->
            if (result is UseCase.Result.Success) {
                shouldShowPaymentInfoTable.value = true
            } else if (result is UseCase.Result.Error) {
                shouldShowPaymentInfoTable.value = false
            }
        }

        shouldShowError = Transformations.map(getPaymentInfoResult) { result ->
            result is UseCase.Result.Error
        }
    }

    fun onRefresh() = loadPaymentInfo()

    private fun loadPaymentInfo() =
        getPaymentInfoResult.addSource(getPaymentInfoUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Success) {
                Timber.d("result.data: %s", result.data)
            } else if (result is UseCase.Result.Error) {
                Timber.w(result.error)
            }
            getPaymentInfoResult.value = result
        }
}
