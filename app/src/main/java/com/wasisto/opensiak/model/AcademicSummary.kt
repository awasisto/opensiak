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

class AcademicSummary(
    val studentPhotoData: ByteArray,
    val studentId: String,
    val studentName: String,
    val batch: Int,
    val majorInd: String,
    val majorEng: String,
    val degreeInd: String,
    val degreeEng: String,
    val academicSupervisor: AcademicSupervisor,
    val academicStatusInd: String,
    val academicStatusEng: String,
    val totalCreditsPassed: Int,
    val totalGradePoints: Float,
    val cumulativeGpa: Float,
    val totalCreditsEarned: Int,
    val gradeStatistics: GradeStatistics
) {
    data class AcademicSupervisor(
        val employeeId: String,
        val name: String
    )

    data class GradeStatistics(
        val gradeCounts: List<GradeCount>
    ) {
        data class GradeCount(
            val grade: String,
            val count: Int
        )
    }
}
