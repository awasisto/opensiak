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
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.wasisto.opensiak.R
import com.wasisto.opensiak.databinding.NavigationDrawerHeaderBinding
import com.wasisto.opensiak.ui.about.AboutActivity
import com.wasisto.opensiak.ui.siak.academicsummary.AcademicSummaryFragment
import com.wasisto.opensiak.ui.siak.classschedule.ClassScheduleFragment
import com.wasisto.opensiak.ui.siak.paymentinfo.PaymentInfoFragment
import com.wasisto.opensiak.ui.signin.SignInActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_siak.*
import javax.inject.Inject

class SiakActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SiakViewModel

    private lateinit var signOutConfirmationDialog: AlertDialog

    private lateinit var startingErrorDialog: AlertDialog

    private lateinit var signOutErrorDialog: AlertDialog

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siak)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this@SiakActivity, viewModelFactory).get(SiakViewModel::class.java)

        NavigationDrawerHeaderBinding.bind(navigationView.getHeaderView(0)).apply {
            viewModel = this@SiakActivity.viewModel
            setLifecycleOwner(this@SiakActivity)
        }

        signOutConfirmationDialog = AlertDialog.Builder(this)
            .setMessage(R.string.sign_out_confirmation_dialog_message)
            .setPositiveButton(R.string.yes) { _, _ -> viewModel.onSignOutConfirmationDialogYesButtonClick() }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .create()

        startingErrorDialog = AlertDialog.Builder(this)
            .setMessage(R.string.starting_error_message)
            .setPositiveButton(R.string.close) { _, _ ->  finish() }
            .setCancelable(false)
            .create()

        signOutErrorDialog = AlertDialog.Builder(this)
            .setMessage(R.string.sign_out_error_message)
            .setPositiveButton(R.string.close) { _, _ ->  finish() }
            .setCancelable(false)
            .create()

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_navigation_drawer,
            R.string.close_navigation_drawer
        ).apply {
            isDrawerSlideAnimationEnabled = false
            syncState()
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                viewModel.onNavigationDrawerClosed()
            }
        })

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.isSaveEnabled = false

        navigationView.setCheckedItem(R.id.navigation_academic_summary)
        onNavigationItemSelected(navigationView.menu.findItem(R.id.navigation_academic_summary))

        viewModel.showSignOutConfirmationDialogEvent.observe(this, Observer {
            if (!signOutConfirmationDialog.isShowing) {
                signOutConfirmationDialog.show()
            }
        })

        viewModel.showStartingErrorEvent.observe(this, Observer {
            if (!startingErrorDialog.isShowing) {
                startingErrorDialog.show()
            }
        })

        viewModel.showSignOutErrorEvent.observe(this, Observer {
            if (!signOutErrorDialog.isShowing) {
                signOutErrorDialog.show()
            }
        })

        viewModel.launchSignInActivityEvent.observe(this, Observer {
            startActivity(Intent(this, SignInActivity::class.java))
        })

        viewModel.finishActivityEvent.observe(this, Observer {
            finish()
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
            R.id.navigation_payment_info -> {
                replaceFragment(PaymentInfoFragment())
                supportActionBar?.title = item.title
            }
            R.id.navigation_class_schedule -> {
                replaceFragment(ClassScheduleFragment())
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
