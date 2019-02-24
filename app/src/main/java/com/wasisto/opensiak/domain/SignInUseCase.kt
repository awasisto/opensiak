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

package com.wasisto.opensiak.domain

import com.wasisto.opensiak.data.account.AccountDataSource
import com.wasisto.opensiak.model.Account
import com.wasisto.opensiak.data.siakng.SiakNgDataSource
import com.wasisto.opensiak.model.Credentials
import com.wasisto.opensiak.util.executor.ExecutorProvider
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val executorProvider: ExecutorProvider,
    private val siakNgDataSource: SiakNgDataSource,
    private val accountDataSource: AccountDataSource
) : UseCase<Credentials, Unit>(executorProvider) {

    override fun execute(params: Credentials) {
        val academicSummaryFuture = executorProvider.computation()
            .submit(Callable { siakNgDataSource.getAcademicSummary(params) })
        val studentProfileFuture = executorProvider.computation()
            .submit(Callable { siakNgDataSource.getStudentProfile(params) })

        try {
            accountDataSource.add(
                Account(
                    username = params.username,
                    password = params.password,
                    name = academicSummaryFuture.get().studentName,
                    email = studentProfileFuture.get().uiEmail,
                    photoData = academicSummaryFuture.get().studentPhotoData
                )
            )
        } catch (e: ExecutionException) {
            throw e.cause!!
        }
    }
}
