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

package com.wasisto.opensiak.di

import com.wasisto.opensiak.ui.about.AboutActivity
import com.wasisto.opensiak.ui.about.AboutModule
import com.wasisto.opensiak.ui.siak.SiakActivity
import com.wasisto.opensiak.ui.siak.SiakModule
import dagger.android.ContributesAndroidInjector
import com.wasisto.opensiak.ui.signin.SignInActivity
import com.wasisto.opensiak.ui.signin.SignInModule
import com.wasisto.opensiak.ui.launcher.LauncherActivity
import com.wasisto.opensiak.ui.launcher.LauncherModule
import dagger.Module

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LauncherModule::class])
    abstract fun contributeSplashActivity(): LauncherActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SignInModule::class])
    abstract fun contributeSignInActivity(): SignInActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [AboutModule::class])
    abstract fun contributeAboutActivity(): AboutActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SiakModule::class])
    abstract fun contributeHomeActivity(): SiakActivity
}
