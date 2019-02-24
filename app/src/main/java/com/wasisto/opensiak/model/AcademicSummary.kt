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

data class AcademicSummary(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AcademicSummary
        if (!studentPhotoData.contentEquals(other.studentPhotoData)) return false
        if (studentId != other.studentId) return false
        if (studentName != other.studentName) return false
        if (batch != other.batch) return false
        if (majorInd != other.majorInd) return false
        if (majorEng != other.majorEng) return false
        if (degreeInd != other.degreeInd) return false
        if (degreeEng != other.degreeEng) return false
        if (academicSupervisor != other.academicSupervisor) return false
        if (academicStatusInd != other.academicStatusInd) return false
        if (academicStatusEng != other.academicStatusEng) return false
        if (totalCreditsPassed != other.totalCreditsPassed) return false
        if (totalGradePoints != other.totalGradePoints) return false
        if (cumulativeGpa != other.cumulativeGpa) return false
        if (totalCreditsEarned != other.totalCreditsEarned) return false
        if (gradeStatistics != other.gradeStatistics) return false
        return true
    }

    override fun hashCode(): Int {
        var result = studentPhotoData.contentHashCode()
        result = 31 * result + studentId.hashCode()
        result = 31 * result + studentName.hashCode()
        result = 31 * result + batch
        result = 31 * result + majorInd.hashCode()
        result = 31 * result + majorEng.hashCode()
        result = 31 * result + degreeInd.hashCode()
        result = 31 * result + degreeEng.hashCode()
        result = 31 * result + academicSupervisor.hashCode()
        result = 31 * result + academicStatusInd.hashCode()
        result = 31 * result + academicStatusEng.hashCode()
        result = 31 * result + totalCreditsPassed
        result = 31 * result + totalGradePoints.hashCode()
        result = 31 * result + cumulativeGpa.hashCode()
        result = 31 * result + totalCreditsEarned
        result = 31 * result + gradeStatistics.hashCode()
        return result
    }

    override fun toString(): String {
        return "AcademicSummary(studentId='$studentId', studentName='$studentName', batch=$batch, " +
                "majorInd='$majorInd', majorEng='$majorEng', degreeInd='$degreeInd', degreeEng='$degreeEng', " +
                "academicSupervisor=$academicSupervisor, academicStatusInd='$academicStatusInd', " +
                "academicStatusEng='$academicStatusEng', totalCreditsPassed=$totalCreditsPassed, " +
                "totalGradePoints=$totalGradePoints, cumulativeGpa=$cumulativeGpa, " +
                "totalCreditsEarned=$totalCreditsEarned, gradeStatistics=$gradeStatistics)"
    }
}
