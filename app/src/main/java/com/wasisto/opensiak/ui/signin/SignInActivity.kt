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

package com.wasisto.opensiak.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.wasisto.opensiak.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.wasisto.opensiak.databinding.ActivitySignInBinding
import com.wasisto.opensiak.ui.about.AboutActivity
import com.wasisto.opensiak.ui.siak.SiakActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class SignInActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignInViewModel::class.java)

        DataBindingUtil.setContentView<ActivitySignInBinding>(this, R.layout.activity_sign_in).apply {
            lifecycleOwner = this@SignInActivity
            viewModel = this@SignInActivity.viewModel
        }

        KeyboardVisibilityEvent.setEventListener(this) { isOpen ->
            aboutButton.visibility = if (isOpen) View.GONE else View.VISIBLE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                currentFocus?.let { view ->
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }

        viewModel.launchSiakActivityEvent.observe(this) { event ->
            if (!event.hasBeenHandled) {
                startActivity(Intent(this, SiakActivity::class.java))
            }
        }

        viewModel.finishActivityEvent.observe(this) {
            finish()
        }

        viewModel.showWrongUsernameOrPasswordEvent.observe(this) { event ->
            if (!event.hasBeenHandled) {
                Snackbar.make(coordinatorLayout, R.string.wrong_username_or_password, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.showAccountAlreadyExistsEvent.observe(this) { event ->
            if (!event.hasBeenHandled) {
                Snackbar.make(coordinatorLayout, R.string.account_already_exists, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.showGeneralSignInErrorEvent.observe(this) { event ->
            if (!event.hasBeenHandled) {
                Snackbar.make(coordinatorLayout, R.string.something_went_wrong, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry) {
                        viewModel.onRetrySignInButtonClick()
                    }
                    .apply {
                        view.findViewById<TextView>(R.id.snackbar_action).apply {
                            isAllCaps = false
                            letterSpacing = 0f
                        }
                    }
                    .show()
            }
        }

        viewModel.launchAboutActivityEvent.observe(this) { event ->
            if (!event.hasBeenHandled) {
                startActivity(Intent(this, AboutActivity::class.java))
            }
        }
    }
}
