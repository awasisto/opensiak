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

import com.wasisto.opensiak.model.Credentials
import com.wasisto.opensiak.model.PaymentInfo
import org.jsoup.Jsoup
import javax.inject.Inject

class PaymentInfoPageScraper @Inject constructor() : PageScraper<Unit, PaymentInfo> {

    override fun scrape(credentials: Credentials, params: Unit): PaymentInfo {
        val siakHttpClient = SiakHttpClient.get(credentials)

        val paymentInfoResponse = siakHttpClient.httpGet("https://academic.ui.ac.id/main/Academic/Payment")

        val paymentInfoDocumentInd = Jsoup.parse(paymentInfoResponse.responseInd.body()!!.string())
        val paymentInfoDocumentEng = Jsoup.parse(paymentInfoResponse.responseEng.body()!!.string())

        val paymentInfoTableTrElementsInd = paymentInfoDocumentInd.select(
            "#ti_m1 > table:nth-child(8) > tbody > tr")
        val paymentInfoTableTrElementsEng = paymentInfoDocumentEng.select(
            "#ti_m1 > table:nth-child(8) > tbody > tr")

        val academicYears = mutableListOf<PaymentInfo.AcademicYear>()

        for (i in 1 until paymentInfoTableTrElementsInd.size - 1) {
            val paymentInfoTableTrElementInd = paymentInfoTableTrElementsInd[i]
            val paymentInfoTableTrElementEng = paymentInfoTableTrElementsEng[i]

            val academicYearAndTermElement = paymentInfoTableTrElementInd.select(
                "td:nth-child(1)").first()
            val academicYearAndTerm = academicYearAndTermElement.text()
            val splitAcademicYearAndTerm = academicYearAndTerm.split(Regex("/|\\s-\\s"))
            val year1 = splitAcademicYearAndTerm[0].toInt()
            val year2 = splitAcademicYearAndTerm[1].toInt()
            val academicYear = academicYears.find { it.year1 == year1 && it.year2 == year2 }
                ?: PaymentInfo.AcademicYear(year1, year2, mutableListOf()).also { academicYears.add(it) }
            val term = splitAcademicYearAndTerm[2].toInt()

            val statusElementInd = paymentInfoTableTrElementInd.select("td:nth-child(9)").first()
            val statusInd = statusElementInd.text()

            val statusElementEng = paymentInfoTableTrElementEng.select("td:nth-child(9)").first()
            val statusEng = statusElementEng.text()

            (academicYear.termPaymentInfoList as MutableList).add(
                PaymentInfo.AcademicYear.TermPaymentInfo(
                    term = term,
                    statusInd = statusInd,
                    statusEng = statusEng
                )
            )
        }

        return PaymentInfo(
            academicYears = academicYears
        )
    }
}