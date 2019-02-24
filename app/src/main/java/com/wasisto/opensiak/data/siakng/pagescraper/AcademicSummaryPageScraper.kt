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

package com.wasisto.opensiak.data.siakng.pagescraper

import com.wasisto.opensiak.model.AcademicSummary
import com.wasisto.opensiak.model.Credentials
import com.wasisto.opensiak.util.executor.ExecutorProvider
import org.jsoup.Jsoup
import javax.inject.Inject

class AcademicSummaryPageScraper @Inject constructor(
    private val executorProvider: ExecutorProvider
) : PageScraper<Unit, AcademicSummary> {

    override fun scrape(credentials: Credentials, params: Unit): AcademicSummary {
        val siakHttpClient = SiakHttpClient.get(credentials, executorProvider)

        val academicSummaryResponse = siakHttpClient.httpGet("https://academic.ui.ac.id/main/Academic/Summary")

        val academicSummaryDocumentInd = Jsoup.parse(academicSummaryResponse.responseInd.body()!!.string())
        val academicSummaryDocumentEng = Jsoup.parse(academicSummaryResponse.responseEng.body()!!.string())

        val studentPhotoElement = academicSummaryDocumentInd.select(
            "#ti_m1 > div.imgsh-box > div.imgsh-in > img").first()
        val studentPhotoSrc = studentPhotoElement.attr("src")
        val studentPhotoResponse = siakHttpClient.httpGet(
            "https://academic.ui.ac.id/main/Academic/$studentPhotoSrc").responseInd
        val studentPhotoData = studentPhotoResponse.body()!!.bytes()

        val studentIdElement = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(1) > td").first()
        val studentId = studentIdElement.text()

        val studentNameElement = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(2) > td").first()
        val studentName = studentNameElement.text()

        val batchElement = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(3) > td").first()
        val batch = Integer.parseInt(batchElement.text())

        val majorAndDegreeElementInd = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(4) > td").first()
        val majorAndDegreeInd = majorAndDegreeElementInd.text().split(Regex(",\\s"))
        val majorInd = majorAndDegreeInd[0]
        val degreeInd = majorAndDegreeInd[1]

        val majorAndDegreeElementEng = academicSummaryDocumentEng.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(4) > td").first()
        val majorAndDegreeEng = majorAndDegreeElementEng.text().split(Regex(",\\s"))
        val majorEng = majorAndDegreeEng[0]
        val degreeEng = majorAndDegreeEng[1]

        val academicSupervisorElement = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(5) > td").first()
        val splitAcademicSupervisor = academicSupervisorElement.text().split(Regex("\\s-\\s"))
        val academicSupervisorEmployeeId = splitAcademicSupervisor[0]
        val academicSupervisorName = splitAcademicSupervisor[1]

        val academicStatusElementInd = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(6) > td").first()
        val academicStatusInd = academicStatusElementInd.text()

        val academicStatusElementEng = academicSummaryDocumentEng.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(6) > td").first()
        val academicStatusEng = academicStatusElementEng.text()

        val totalCreditsPassedElement = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(7) > td").first()
        val totalCreditsPassed = Integer.parseInt(totalCreditsPassedElement.text())

        val totalGradePointsElement = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(8) > td").first()
        val totalGradePoints = java.lang.Float.parseFloat(totalGradePointsElement.text())

        val cumulativeGpaElement = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(9) > td").first()
        val cumulativeGpa = java.lang.Float.parseFloat(cumulativeGpaElement.text())

        val totalCreditsEarnedElement = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(4) > tbody > tr:nth-child(10) > td").first()
        val totalCreditsEarned = Integer.parseInt(totalCreditsEarnedElement.text())

        val gradeStatisticsGradeCounts = mutableListOf<AcademicSummary.GradeStatistics.GradeCount>()

        val gradeCountsTableTrElements = academicSummaryDocumentInd.select(
            "#ti_m1 > table:nth-child(7) > tbody > tr")

        for (i in 1 until gradeCountsTableTrElements.size - 1) {
            val gradeCountsTableTrElement = gradeCountsTableTrElements[i]

            val gradeElement = gradeCountsTableTrElement.select("td:nth-child(1)").first()
            val grade = gradeElement.text()

            val countElement = gradeCountsTableTrElement.select("td:nth-child(2)").first()
            val count = Integer.parseInt(countElement.text())

            gradeStatisticsGradeCounts += AcademicSummary.GradeStatistics.GradeCount(
                grade = grade,
                count = count
            )
        }

        return AcademicSummary(
            studentPhotoData = studentPhotoData,
            studentId = studentId,
            studentName = studentName,
            batch = batch,
            majorInd = majorInd,
            majorEng = majorEng,
            degreeInd = degreeInd,
            degreeEng = degreeEng,
            academicSupervisor = AcademicSummary.AcademicSupervisor(
                employeeId = academicSupervisorEmployeeId,
                name = academicSupervisorName
            ),
            academicStatusInd = academicStatusInd,
            academicStatusEng = academicStatusEng,
            totalCreditsPassed = totalCreditsPassed,
            totalGradePoints = totalGradePoints,
            cumulativeGpa = cumulativeGpa,
            totalCreditsEarned = totalCreditsEarned,
            gradeStatistics = AcademicSummary.GradeStatistics(
                gradeCounts = gradeStatisticsGradeCounts
            )
        )
    }
}
