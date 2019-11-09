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

package com.wasisto.opensiak.data.account

import android.content.Context

import com.wasisto.opensiak.R
import com.wasisto.opensiak.exception.AccountAlreadyExistsException
import com.wasisto.opensiak.model.Account

import java.util.Date

import javax.inject.Inject

class FakeAccountDataSource @Inject constructor(context: Context) : AccountDataSource {

    private val accounts = mutableMapOf<String, Account>()

    init {
        accounts["ali.connors@ui.example"] = Account(
            username = "ali.connors",
            password = "password",
            name = "Ali Connors",
            email = "ali.connors@ui.example",
            photoData = context.resources.openRawResource(R.raw.account_photo_1).readBytes(),
            lastActive = Date()
        )

        accounts["jonathan.lee@ui.example"] = Account(
            username = "jonathan.lee",
            password = "password",
            name = "Jonathan Lee",
            email = "jonathan.lee@ui.example",
            photoData = context.resources.openRawResource(R.raw.account_photo_2).readBytes(),
            lastActive = Date(accounts["ali.connors@ui.example"]!!.lastActive.time - 1)
        )
    }

    override fun getAll(): List<Account> {
        return accounts.values.toList()
    }

    override fun getByEmail(email: String): Account {
        return accounts.getValue(email)
    }

    override fun getLastAccountActive(): Account {
        return accounts.values.sortedWith(Comparator { account1, account2 -> account2.lastActive.compareTo(account1.lastActive) }).first()
    }

    override fun add(account: Account) {
        if (accounts.containsKey(account.email)) {
            throw AccountAlreadyExistsException()
        }
        accounts[account.email] = account
    }

    override fun update(account: Account) {
        accounts[account.email] = account
    }

    override fun remove(account: Account) {
        accounts.remove(account.email)
    }
}
