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
import com.wasisto.opensiak.model.*

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
                    credentials.username
                        .replace("\\.".toRegex(), " ")
                        .replace("\\d".toRegex(), "")
                        .split(" ")
                        .joinToString(separator = " ") { it.capitalize() }
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
            totalCreditsPassed = 36,
            totalGradePoints = 112.80f,
            cumulativeGpa = 3.13f,
            totalCreditsEarned = 36,
            gradeStatistics = AcademicSummary.GradeStatistics(
                gradeCounts = listOf(
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "A",
                        count = 1
                    ),
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "B+",
                        count = 3
                    ),
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "B",
                        count = 1
                    ),
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "B-",
                        count = 3
                    ),
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "C",
                        count = 1
                    ),
                    AcademicSummary.GradeStatistics.GradeCount(
                        grade = "D",
                        count = 1
                    )
                )
            )
        )
    }

    override fun getPaymentInfo(credentials: Credentials): PaymentInfo {
        return PaymentInfo(
            academicYears = listOf(
                PaymentInfo.AcademicYear(
                    year1 = 2017,
                    year2 = 2018,
                    termPaymentInfoList = listOf(
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 2,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        ),
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 1,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        )
                    )
                ),
                PaymentInfo.AcademicYear(
                    year1 = 2016,
                    year2 = 2017,
                    termPaymentInfoList = listOf(
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 3,
                            statusInd = "Tidak Ada Tagihan",
                            statusEng = "No Dues"
                        ),
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 2,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        ),
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 1,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        )
                    )
                ),
                PaymentInfo.AcademicYear(
                    year1 = 2015,
                    year2 = 2016,
                    termPaymentInfoList = listOf(
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 3,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        ),
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 2,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        ),
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 1,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        )
                    )
                ),
                PaymentInfo.AcademicYear(
                    year1 = 2014,
                    year2 = 2015,
                    termPaymentInfoList = listOf(
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 3,
                            statusInd = "Tidak Ada Tagihan",
                            statusEng = "No Dues"
                        ),
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 2,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        ),
                        PaymentInfo.AcademicYear.TermPaymentInfo(
                            term = 1,
                            statusInd = "Lunas",
                            statusEng = "Paid"
                        )
                    )
                )
            )
        )
    }

    override fun getClassSchedule(credentials: Credentials): ClassSchedule {
        return ClassSchedule(
            days = listOf(
                ClassSchedule.Day(
                    dayOfWeek = DayOfWeek.MONDAY,
                    classes = listOf(
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("08:00"),
                            endTime = LocalTime.parse("09:40"),
                            courseNameInd = "Analisis Numerik",
                            courseNameEng = "Numerical Analysis",
                            room = "2.2405"
                        ),
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("13:00"),
                            endTime = LocalTime.parse("14:40"),
                            courseNameInd = "Teori Bahasa & Automata",
                            courseNameEng = "Automata & Theory of Languages",
                            room = "2.2405"
                        ),
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("16:00"),
                            endTime = LocalTime.parse("17:40"),
                            courseNameInd = "Layanan Aplikasi Web",
                            courseNameEng = "Web Services and Applications",
                            room = "2.2604"
                        )
                    )
                ),
                ClassSchedule.Day(
                    dayOfWeek = DayOfWeek.TUESDAY,
                    classes = emptyList()
                ),
                ClassSchedule.Day(
                    dayOfWeek = DayOfWeek.WEDNESDAY,
                    classes = listOf(
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("10:00"),
                            endTime = LocalTime.parse("11:40"),
                            courseNameInd = "Data Science & Analytics",
                            courseNameEng = "Data Science & Analytics",
                            room = "3.3113"
                        ),
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("12:00"),
                            endTime = LocalTime.parse("12:30"),
                            courseNameInd = "Kerja Praktik",
                            courseNameEng = "Internship",
                            room = "2.2401"
                        ),
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("14:00"),
                            endTime = LocalTime.parse("15:40"),
                            courseNameInd = "Teori Bahasa & Automata",
                            courseNameEng = "Automata & Theory of Languages",
                            room = "2.2405"
                        ),
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("16:00"),
                            endTime = LocalTime.parse("17:40"),
                            courseNameInd = "Layanan Aplikasi Web",
                            courseNameEng = "Web Services and Applications",
                            room = "A1.03 (Ged Baru)"
                        )
                    )
                ),
                ClassSchedule.Day(
                    dayOfWeek = DayOfWeek.THURSDAY,
                    classes = listOf(
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("08:00"),
                            endTime = LocalTime.parse("08:50"),
                            courseNameInd = "Analisis Numerik",
                            courseNameEng = "Numerical Analysis",
                            room = "2.2405"
                        )
                    )
                ),
                ClassSchedule.Day(
                    dayOfWeek = DayOfWeek.FRIDAY,
                    classes = listOf(
                        ClassSchedule.Day.Class(
                            startTime = LocalTime.parse("09:00"),
                            endTime = LocalTime.parse("09:50"),
                            courseNameInd = "Data Science & Analytics",
                            courseNameEng = "Data Science & Analytics",
                            room = "3.3113"
                        )
                    )
                ),
                ClassSchedule.Day(
                    dayOfWeek = DayOfWeek.SATURDAY,
                    classes = emptyList()
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
