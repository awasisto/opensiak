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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wasisto.opensiak.databinding.FragmentAcademicSummaryBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_academic_summary.*
import kotlinx.android.synthetic.main.layout_error.*
import javax.inject.Inject

class AcademicSummaryFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AcademicSummaryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this@AcademicSummaryFragment, viewModelFactory)
            .get(AcademicSummaryViewModel::class.java)

        val binding = FragmentAcademicSummaryBinding.inflate(inflater, container, false).apply {
            viewModel = this@AcademicSummaryFragment.viewModel
            setLifecycleOwner(this@AcademicSummaryFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipeRefreshLayout.setOnRefreshListener { viewModel.onRefresh() }

        retryButton.setOnClickListener { viewModel.onRefresh() }
    }
}
