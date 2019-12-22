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

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.opensiak.model.PaymentInfo

@BindingAdapter("paymentInfoAcademicYears")
fun paymentInfoAcademicYears(recyclerView: RecyclerView, academicYears: List<PaymentInfo.AcademicYear>?) {
    academicYears?.let {
        recyclerView.adapter = (recyclerView.adapter as? AcademicYearsAdapter ?: AcademicYearsAdapter()).apply {
            data = academicYears
        }
    }
}

@BindingAdapter("termPaymentInfoList")
fun termPaymentInfoList(recyclerView: RecyclerView, termPaymentInfoList: List<PaymentInfo.AcademicYear.TermPaymentInfo>?) {
    termPaymentInfoList?.let {
        recyclerView.adapter = (recyclerView.adapter as? TermPaymentInfoAdapter ?: TermPaymentInfoAdapter()).apply {
            data = termPaymentInfoList
        }
    }
}
