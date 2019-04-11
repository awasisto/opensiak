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

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalTime

data class ClassSchedule(
    val days: List<Day>
) {
    data class Day(
        val dayOfWeek: DayOfWeek,
        val classes: List<Class>
    ) {
        data class Class(
            val startTime: LocalTime,
            val endTime: LocalTime,
            val courseNameInd: String,
            val courseNameEng: String,
            val room: String
        )
    }
}
