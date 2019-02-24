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

package com.wasisto.opensiak.ui.siak.academicsummary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.opensiak.databinding.ItemGradeCountBinding
import com.wasisto.opensiak.model.AcademicSummary
import java.util.*

class GradeCountsAdapter : RecyclerView.Adapter<GradeCountsAdapter.ViewHolder>() {

    lateinit var data: List<AcademicSummary.GradeStatistics.GradeCount>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemGradeCountBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val binding: ItemGradeCountBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gradeCount: AcademicSummary.GradeStatistics.GradeCount) {
            binding.grade = gradeCount.grade
            binding.count = String.format(Locale.getDefault(), "%d", gradeCount.count)
        }
    }
}
