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
import com.wasisto.opensiak.model.StudentProfile
import org.jsoup.Jsoup
import javax.inject.Inject

class StudentProfilePageScraper @Inject constructor() : PageScraper<Unit, StudentProfile> {

    override fun scrape(credentials: Credentials, params: Unit): StudentProfile {
        val siakHttpClient = SiakHttpClient.get(credentials)

        val studentProfileResponse = siakHttpClient.httpGet("https://academic.ui.ac.id/main/Student/IDMView")

        val studentProfileDocument = Jsoup.parse(studentProfileResponse.responseInd.body()!!.string())

        val uiEmailElement = studentProfileDocument.select(
            "#ti_m1 > table > tbody > tr:nth-child(17) > td:nth-child(2)").first()
        val uiEmail = uiEmailElement.text()

        return StudentProfile(
            uiEmail = uiEmail
        )
    }
}