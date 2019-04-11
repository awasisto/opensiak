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

package com.wasisto.opensiak.model

data class PaymentInfo(
    val academicYears: List<AcademicYear>
) {
    data class AcademicYear(
        val year1: Int,
        val year2: Int,
        val termPaymentInfoList: List<TermPaymentInfo>
    ) {
        data class TermPaymentInfo(
            val term: Int,
            val statusInd: String,
            val statusEng: String
        )
    }
}
