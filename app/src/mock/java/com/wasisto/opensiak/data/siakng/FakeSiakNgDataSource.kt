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

import android.content.Context

import com.wasisto.opensiak.R
import com.wasisto.opensiak.model.AcademicSummary
import com.wasisto.opensiak.model.CoursePlanSchedule
import com.wasisto.opensiak.model.Credentials
import com.wasisto.opensiak.model.StudentProfile

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalTime

import javax.inject.Inject

class FakeSiakNgDataSource @Inject constructor(private val context: Context) : SiakNgDataSource {

    override fun getAcademicSummary(credentials: Credentials): AcademicSummary {
        return AcademicSummary(
            studentPhotoData = when (credentials.username) {
                "ali.connors" -> context.resources.openRawResource(R.raw.account_photo_1).readBytes()
                "jonathan.lee" -> context.resources.openRawResource(R.raw.account_photo_2).readBytes()
                else -> context.resources.openRawResource(R.raw.account_photo_0).readBytes()
            },
            studentId = "1403552974",
            studentName = when (credentials.username) {
                "ali.connors" -> "Ali Connors"
                "jonathan.lee" -> "Jonathan Lee"
                else -> {
                    val str = credentials.username.replace("\\.".toRegex(), "")
                        .replace("\\d".toRegex(), "")
                    val split = str.split(" ".toRegex())
                    val stringBuilder = StringBuilder()
                    for (s in split) {
                        stringBuilder.append(s.substring(0, 1).toUpperCase()).append(
                            s.substring(1).toLowerCase()).append(" ")
                    }
                    stringBuilder.toString()
                }
            },
            batch = 2014,
            majorInd = "Ilmu Komputer",
            majorEng = "Computer Science",
            degreeInd = "S1 Reguler",
            degreeEng = "Undergraduate",
            academicSupervisor = AcademicSummary.AcademicSupervisor(
                employeeId = "1584226144",
                name = "Sandra Adams"
            ),
            academicStatusInd = "Aktif",
            academicStatusEng = "Active",
            totalCreditsPassed = 20,
            totalGradePoints = 15.3f,
            cumulativeGpa = 3.26f,
            totalCreditsEarned = 20,
            gradeStatistics = AcademicSummary.GradeStatistics(
                gradeCounts = listOf(
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "A",
                        count = 1
                    ),
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "B+",
                        count = 2
                    ),
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "B-",
                        count = 1
                    ),
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "C",
                        count = 1
                    )
                )
            )
        )
    }

    override fun getCoursePlanSchedule(credentials: Credentials): CoursePlanSchedule {
        return CoursePlanSchedule(
            days = listOf(
                CoursePlanSchedule.Day(
                    dayOfWeek = DayOfWeek.MONDAY,
                    classes = listOf(
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("08:00"),
                            endTime = LocalTime.parse("09:40"),
                            courseNameInd = "Analisis Numerik",
                            courseNameEng = "Numerical Analysis",
                            room = "2.2405"
                        ),
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("13:00"),
                            endTime = LocalTime.parse("14:40"),
                            courseNameInd = "Teori Bahasa & Automata",
                            courseNameEng = "Automata & Theory of Languages",
                            room = "2.2405"
                        ),
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("16:00"),
                            endTime = LocalTime.parse("17:40"),
                            courseNameInd = "Layanan Aplikasi Web",
                            courseNameEng = "Web Services and Applications",
                            room = "2.2604"
                        )
                    )
                ),
                CoursePlanSchedule.Day(
                    dayOfWeek = DayOfWeek.TUESDAY,
                    classes = emptyList()
                ),
                CoursePlanSchedule.Day(
                    dayOfWeek = DayOfWeek.WEDNESDAY,
                    classes = listOf(
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("10:00"),
                            endTime = LocalTime.parse("11:40"),
                            courseNameInd = "Data Science & Analytics",
                            courseNameEng = "Data Science & Analytics",
                            room = "3.3113"
                        ),
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("12:00"),
                            endTime = LocalTime.parse("12:30"),
                            courseNameInd = "Kerja Praktik",
                            courseNameEng = "Internship",
                            room = "2.2401"
                        ),
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("14:00"),
                            endTime = LocalTime.parse("15:40"),
                            courseNameInd = "Teori Bahasa & Automata",
                            courseNameEng = "Automata & Theory of Languages",
                            room = "2.2405"
                        ),
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("16:00"),
                            endTime = LocalTime.parse("17:40"),
                            courseNameInd = "Layanan Aplikasi Web",
                            courseNameEng = "Web Services and Applications",
                            room = "A1.03 (Ged Baru)"
                        )
                    )
                ),
                CoursePlanSchedule.Day(
                    dayOfWeek = DayOfWeek.THURSDAY,
                    classes = listOf(
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("08:00"),
                            endTime = LocalTime.parse("08:50"),
                            courseNameInd = "Analisis Numerik",
                            courseNameEng = "Numerical Analysis",
                            room = "2.2405"
                        )
                    )
                ),
                CoursePlanSchedule.Day(
                    dayOfWeek = DayOfWeek.FRIDAY,
                    classes = listOf(
                        CoursePlanSchedule.Day.Class(
                            startTime = LocalTime.parse("09:00"),
                            endTime = LocalTime.parse("09:50"),
                            courseNameInd = "Data Science & Analytics",
                            courseNameEng = "Data Science & Analytics",
                            room = "3.3113"
                        )
                    )
                )
            )
        )
    }

    override fun getStudentProfile(credentials: Credentials): StudentProfile {
        return StudentProfile(
            uiEmail = when (credentials.username) {
                "ali.connors" -> "ali.connors@ui.example"
                "jonathan.lee" -> "jonathan.lee@ui.example"
                else ->  "${credentials.username}@ui.example"
            }
        )
    }
}
