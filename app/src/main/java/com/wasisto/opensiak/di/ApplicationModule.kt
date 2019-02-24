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

import android.content.Context
import com.wasisto.androidkeystoreencryption.EncryptionService
import com.wasisto.opensiak.OpenSiakApplication
import com.wasisto.opensiak.util.executor.ExecutorProvider
import com.wasisto.opensiak.util.executor.ExecutorProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class ApplicationModule {

    @Singleton
    @Binds
    abstract fun bindContext(application: OpenSiakApplication): Context

    @Singleton
    @Binds
    abstract fun bindExecutorProvider(executorProvider: ExecutorProviderImpl): ExecutorProvider

    @Module
    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideEncryptionService(context: Context): EncryptionService {
            return EncryptionService.getInstance(context)
        }
    }
}
