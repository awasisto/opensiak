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

package com.wasisto.opensiak.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.wasisto.opensiak.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import com.wasisto.opensiak.databinding.ActivityAboutBinding
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AboutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AboutViewModel::class.java)

        DataBindingUtil.setContentView<ActivityAboutBinding>(this, R.layout.activity_about).apply {
            lifecycleOwner = this@AboutActivity
            viewModel = this@AboutActivity.viewModel
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.launchBrowserEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { url ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(url))))
            }
        }

        viewModel.launchOssLicensesMenuActivity.observe(this) { event ->
            if (!event.hasBeenHandled) {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licenses))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }
}
