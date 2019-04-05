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

package com.wasisto.opensiak.ui.siak

import androidx.lifecycle.ViewModel
import com.wasisto.opensiak.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.wasisto.opensiak.ui.siak.academicsummary.AcademicSummaryFragment
import dagger.android.ContributesAndroidInjector
import com.wasisto.opensiak.di.FragmentScoped
import com.wasisto.opensiak.ui.siak.academicsummary.AcademicSummaryModule
import com.wasisto.opensiak.ui.siak.courseplanschedule.CoursePlanScheduleFragment
import com.wasisto.opensiak.ui.siak.courseplanschedule.CoursePlanScheduleModule
import com.wasisto.opensiak.ui.siak.paymentinfo.PaymentInfoFragment
import com.wasisto.opensiak.ui.siak.paymentinfo.PaymentInfoModule

@Module
abstract class SiakModule {

    @Binds
    @IntoMap
    @ViewModelKey(SiakViewModel::class)
    abstract fun bindViewModel(viewModel: SiakViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector(modules = [AcademicSummaryModule::class])
    abstract fun contributeAcademicSummaryFragment(): AcademicSummaryFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [PaymentInfoModule::class])
    abstract fun contributePaymentInfoFragment(): PaymentInfoFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [CoursePlanScheduleModule::class])
    abstract fun contributeCoursePlanScheduleFragment(): CoursePlanScheduleFragment
}
