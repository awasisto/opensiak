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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.opensiak.R
import com.wasisto.opensiak.databinding.ItemDayBinding
import com.wasisto.opensiak.model.ClassSchedule
import org.threeten.bp.DayOfWeek

class DaysAdapter : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    lateinit var data: List<ClassSchedule.Day>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(day: ClassSchedule.Day) {
            binding.dayNameResId = when (day.dayOfWeek) {
                DayOfWeek.MONDAY ->  R.string.mon
                DayOfWeek.TUESDAY ->  R.string.tue
                DayOfWeek.WEDNESDAY ->  R.string.wed
                DayOfWeek.THURSDAY ->  R.string.thu
                DayOfWeek.FRIDAY ->  R.string.fri
                DayOfWeek.SATURDAY ->  R.string.sat
                DayOfWeek.SUNDAY ->  R.string.sun
            }

            binding.classes = day.classes
        }
    }
}
