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

package com.wasisto.opensiak.ui.siak.paymentinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.opensiak.databinding.ItemPaymentInfoAcademicYearBinding
import com.wasisto.opensiak.model.PaymentInfo

class AcademicYearsAdapter : RecyclerView.Adapter<AcademicYearsAdapter.ViewHolder>() {

    lateinit var data: List<PaymentInfo.AcademicYear>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemPaymentInfoAcademicYearBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val binding: ItemPaymentInfoAcademicYearBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(academicYear: PaymentInfo.AcademicYear) {
            val formattedYear2 = if (academicYear.year2 / 100 == academicYear.year1 / 100) {
                academicYear.year2 % 100
            } else {
                academicYear.year2
            }
            binding.academicYear = "${academicYear.year1}-$formattedYear2"
            binding.termPaymentInfoList = academicYear.termPaymentInfoList
        }
    }
}
