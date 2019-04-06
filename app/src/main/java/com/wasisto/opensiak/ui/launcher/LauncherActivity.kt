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

package com.wasisto.opensiak.ui.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wasisto.opensiak.R
import com.wasisto.opensiak.ui.siak.SiakActivity
import com.wasisto.opensiak.ui.signin.SignInActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class LauncherActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var startingErrorDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(LauncherViewModel::class.java)

        startingErrorDialog = AlertDialog.Builder(this)
            .setMessage(R.string.starting_error_message)
            .setPositiveButton(R.string.close) { _, _ ->  finish() }
            .setCancelable(false)
            .create()

        viewModel.launchSiakActivityEvent.observe(this, Observer {
            startActivity(Intent(this, SiakActivity::class.java))
        })

        viewModel.launchSignInActivityEvent.observe(this, Observer {
            startActivity(Intent(this, SignInActivity::class.java))
        })

        viewModel.finishActivityEvent.observe(this, Observer {
            finish()
        })

        viewModel.showStartingErrorEvent.observe(this, Observer {
            if (!startingErrorDialog.isShowing) {
                startingErrorDialog.show()
            }
        })
    }
}
