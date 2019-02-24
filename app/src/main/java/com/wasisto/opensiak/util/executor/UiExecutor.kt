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

package com.wasisto.opensiak.util.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.TimeUnit

object UiExecutor : AbstractExecutorService() {

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        when {
            handler.looper == Looper.myLooper() -> command.run()
            else -> handler.post(command)
        }
    }

    override fun shutdown() = throw UnsupportedOperationException()

    override fun shutdownNow() = throw UnsupportedOperationException()

    override fun isShutdown() = false

    override fun isTerminated() = false

    override fun awaitTermination(timeout: Long, timeUnit: TimeUnit) = throw UnsupportedOperationException()
}