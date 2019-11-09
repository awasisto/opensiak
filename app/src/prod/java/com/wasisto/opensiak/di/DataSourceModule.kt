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
import androidx.room.Room
import com.wasisto.opensiak.data.account.AccountDao
import com.wasisto.opensiak.data.account.AccountDataSource
import com.wasisto.opensiak.data.account.AccountDataSourceImpl
import com.wasisto.opensiak.data.account.AccountDatabase
import com.wasisto.opensiak.data.siakng.SiakNgDataSource
import com.wasisto.opensiak.data.siakng.SiakNgDataSourceImpl

import javax.inject.Singleton

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindSiakNgDataSource(siakNgDataSource: SiakNgDataSourceImpl): SiakNgDataSource

    @Singleton
    @Binds
    abstract fun bindAccountDataSource(accountDataSource: AccountDataSourceImpl): AccountDataSource

    @Module
    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideAccountDao(context: Context): AccountDao {
            return Room.databaseBuilder(context, AccountDatabase::class.java, "accounts.db").build().accountDao()
        }
    }
}
