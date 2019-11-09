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

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

interface UseCase<in P, R> {

    fun execute(params: P): R

    fun executeAsync(params: P): LiveData<Result<R>> {
        return liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                emit(Result.Success(execute(params)))
            } catch (error: Throwable) {
                emit(Result.Error(error))
            }
        }
    }

    sealed class Result<out T> {
        class Success<out T>(val data: T) : Result<T>()
        class Error(val error: Throwable) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }
}
