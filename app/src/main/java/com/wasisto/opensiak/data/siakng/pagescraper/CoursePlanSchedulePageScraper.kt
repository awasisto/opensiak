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

import com.wasisto.opensiak.model.CoursePlanSchedule
import com.wasisto.opensiak.model.Credentials
import com.wasisto.opensiak.util.executor.ExecutorProvider
import org.jsoup.Jsoup
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalTime
import javax.inject.Inject

class CoursePlanSchedulePageScraper @Inject constructor(
    private val executorProvider: ExecutorProvider
) : PageScraper<Unit, CoursePlanSchedule> {

    override fun scrape(credentials: Credentials, params: Unit): CoursePlanSchedule {
        val siakHttpClient = SiakHttpClient.get(credentials, executorProvider)

        val coursePlanScheduleResponse = siakHttpClient.httpGet(
            "https://academic.ui.ac.id/main/CoursePlan/CoursePlanViewSchedule")

        val coursePlanScheduleDocumentInd = Jsoup.parse(coursePlanScheduleResponse.responseInd.body()!!.string())
        val coursePlanScheduleDocumentEng = Jsoup.parse(coursePlanScheduleResponse.responseEng.body()!!.string())

        val dayTdElementsInd = coursePlanScheduleDocumentInd.select("#ti_m1 > table td")
        val dayTdElementsEng = coursePlanScheduleDocumentEng.select("#ti_m1 > table td")

        val days = mutableListOf<CoursePlanSchedule.Day>()

        for (i in 1..6) {
            val dayOfWeek: DayOfWeek = when (i) {
                1 -> DayOfWeek.MONDAY
                2 -> DayOfWeek.TUESDAY
                3 -> DayOfWeek.WEDNESDAY
                4 -> DayOfWeek.THURSDAY
                5 -> DayOfWeek.FRIDAY
                6 -> DayOfWeek.SATURDAY
                else -> throw RuntimeException("i: $i")
            }

            val classElementsInd = dayTdElementsInd[i].select(".sch-inner")
            val classElementsEng = dayTdElementsEng[i].select(".sch-inner")

            val classes = mutableListOf<CoursePlanSchedule.Day.Class>()

            for (j in classElementsInd.indices) {
                val classElementInd = classElementsInd[j]
                val classElementEng = classElementsEng[j]

                val courseNameAndRoomElementInd = classElementInd.select("p").first()
                val courseNameAndRoomInd = courseNameAndRoomElementInd.text().split(Regex("\\sRuang:\\s"))
                val classCourseNameInd = courseNameAndRoomInd[0].trim()
                val classRoom = courseNameAndRoomInd[1].trim()

                val courseNameAndRoomElementEng = classElementEng.select("p").first()
                val courseNameAndRoomEng = courseNameAndRoomElementEng.text().split(Regex("\\sRoom:\\s"))
                val classCourseNameEng = courseNameAndRoomEng[0].trim()

                val timeText = classElementInd.select("h3").first().text().replace(
                    Regex("\\."), ":")
                val classStartTime = LocalTime.parse(timeText.substring(0, 5))
                val classEndTime = LocalTime.parse(timeText.substring(8, 13))

                classes.add(
                    CoursePlanSchedule.Day.Class(
                        startTime = classStartTime,
                        endTime = classEndTime,
                        courseNameInd = classCourseNameInd,
                        courseNameEng = classCourseNameEng,
                        room = classRoom
                    )
                )
            }

            days.add(
                CoursePlanSchedule.Day(
                    dayOfWeek = dayOfWeek,
                    classes = classes
                )
            )
        }

        return CoursePlanSchedule(
            days = days
        )
    }
}