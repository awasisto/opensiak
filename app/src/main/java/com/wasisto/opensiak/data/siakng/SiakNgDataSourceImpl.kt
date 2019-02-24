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

package com.wasisto.opensiak.data.siakng

import com.wasisto.opensiak.data.siakng.pagescraper.AcademicSummaryPageScraper
import com.wasisto.opensiak.data.siakng.pagescraper.CoursePlanSchedulePageScraper
import com.wasisto.opensiak.data.siakng.pagescraper.StudentProfilePageScraper
import com.wasisto.opensiak.model.Credentials
import javax.inject.Inject

class SiakNgDataSourceImpl @Inject constructor(
    private val academicSummaryPageScraper: AcademicSummaryPageScraper,
    private val coursePlanSchedulePageScraper: CoursePlanSchedulePageScraper,
    private val studentProfilePageScraper: StudentProfilePageScraper
) : SiakNgDataSource {

    override fun getAcademicSummary(credentials: Credentials) = academicSummaryPageScraper.scrape(credentials, Unit)

    override fun getCoursePlanSchedule(credentials: Credentials) =
        coursePlanSchedulePageScraper.scrape(credentials, Unit)

    override fun getStudentProfile(credentials: Credentials) = studentProfilePageScraper.scrape(credentials, Unit)
}
