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

package com.wasisto.opensiak.usecase

import com.wasisto.opensiak.data.account.AccountDataSource
import com.wasisto.opensiak.data.siakng.SiakNgDataSource
import com.wasisto.opensiak.model.AcademicSummary
import com.wasisto.opensiak.model.Credentials
import javax.inject.Inject

class GetAcademicSummaryUseCase @Inject constructor(
    private val siakNgDataSource: SiakNgDataSource,
    private val accountDataSource: AccountDataSource
) : UseCase<Unit, AcademicSummary> {

    override fun execute(params: Unit): AcademicSummary {
        return siakNgDataSource.getAcademicSummary(Credentials.fromAccount(accountDataSource.getLastAccountActive()))
    }
}