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

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.opensiak.databinding.ItemClassBinding
import com.wasisto.opensiak.model.ClassSchedule
import com.wasisto.opensiak.util.isDefaultLocaleLanguageIndonesian
import org.threeten.bp.Duration
import org.threeten.bp.format.DateTimeFormatter

class ClassesAdapter : RecyclerView.Adapter<ClassesAdapter.ViewHolder>() {

    lateinit var data: List<ClassSchedule.Day.Class>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ViewHolder(private val binding: ItemClassBinding): RecyclerView.ViewHolder(binding.root) {

        companion object {
            private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        }

        fun bind(cls: ClassSchedule.Day.Class) {
            binding.name = if (isDefaultLocaleLanguageIndonesian()) cls.courseNameInd else cls.courseNameEng

            binding.time = "${timeFormatter.format(cls.startTime)} – ${timeFormatter.format(cls.endTime)}"

            binding.room = cls.room

            if (Duration.between(cls.startTime, cls.endTime) > Duration.ofHours(1)) {
                binding.classLayout.minimumHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 112f, binding.classLayout.context.resources.displayMetrics).toInt()
            }
        }
    }
}