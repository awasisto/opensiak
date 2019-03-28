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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wasisto.opensiak.util.executor.ExecutorProvider

abstract class UseCase<P, R> constructor(private val executorProvider: ExecutorProvider) {

    fun executeAsync(params: P): LiveData<Result<R>> {
        val result = MutableLiveData<Result<R>>()
        result.value = Result.Loading
        executorProvider.io().submit {
            try {
                result.postValue(Result.Success(execute(params)))
            } catch (error: Throwable) {
                result.postValue(Result.Error(error))
            }
        }
        return result
    }

    abstract fun execute(params: P): R

    sealed class Result<out R> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val error: Throwable) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }
}
