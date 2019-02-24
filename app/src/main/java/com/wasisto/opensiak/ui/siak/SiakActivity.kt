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

package com.wasisto.opensiak.ui.siak

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.wasisto.opensiak.R
import com.wasisto.opensiak.databinding.NavigationDrawerHeaderBinding
import com.wasisto.opensiak.ui.about.AboutActivity
import com.wasisto.opensiak.ui.siak.academicsummary.AcademicSummaryFragment
import com.wasisto.opensiak.ui.siak.courseplanschedule.CoursePlanScheduleFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_siak.*
import javax.inject.Inject

class SiakActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SiakViewModel

    private lateinit var errorDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siak)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        viewModel = ViewModelProviders.of(this@SiakActivity, viewModelFactory).get(SiakViewModel::class.java)

        NavigationDrawerHeaderBinding.bind(navigationView.getHeaderView(0)).apply {
            viewModel = this@SiakActivity.viewModel
            setLifecycleOwner(this@SiakActivity)
        }

        errorDialog = AlertDialog.Builder(this)
            .setMessage(R.string.starting_error_message)
            .setPositiveButton(R.string.close) { _, _ ->  finish() }
            .setCancelable(false)
            .create()

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.isSaveEnabled = false

        navigationView.setCheckedItem(R.id.navigation_academic_summary)
        onNavigationItemSelected(navigationView.menu.findItem(R.id.navigation_academic_summary))

        viewModel.showErrorEvent.observe(this, Observer {
            if (!errorDialog.isShowing) {
                errorDialog.show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            drawerLayout.openDrawer(GravityCompat.START)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_academic_summary -> {
                replaceFragment(AcademicSummaryFragment())
                supportActionBar?.title = item.title
            }
            R.id.navigation_course_plan_schedule -> {
                replaceFragment(CoursePlanScheduleFragment())
                supportActionBar?.title = item.title
            }
            R.id.navigation_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            navigationView.checkedItem?.itemId != R.id.navigation_academic_summary -> {
                navigationView.setCheckedItem(R.id.navigation_academic_summary)
                onNavigationItemSelected(navigationView.menu.findItem(R.id.navigation_academic_summary))
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().replace(R.id.contentFrame, fragment).commit()
}
