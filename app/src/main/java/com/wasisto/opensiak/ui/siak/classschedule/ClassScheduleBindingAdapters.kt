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

package com.wasisto.opensiak.ui.siak.classschedule

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.opensiak.model.ClassSchedule

@BindingAdapter("days")
fun days(recyclerView: RecyclerView, days: List<ClassSchedule.Day>?) {
    days?.let {
        recyclerView.adapter = (recyclerView.adapter as? DaysAdapter ?: DaysAdapter()).apply {
            data = days
        }
    }
}

@BindingAdapter("classes")
fun classes(recyclerView: RecyclerView, classes: List<ClassSchedule.Day.Class>?) {
    classes?.let {
        recyclerView.adapter = (recyclerView.adapter as? ClassesAdapter ?: ClassesAdapter()).apply {
            data = classes
        }
    }
}
