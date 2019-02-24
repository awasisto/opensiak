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

import android.database.sqlite.SQLiteConstraintException
import com.wasisto.androidkeystoreencryption.EncryptionService
import com.wasisto.androidkeystoreencryption.model.EncryptedDataAndIv
import com.wasisto.opensiak.model.Account

import javax.inject.Inject

class AccountDataSourceImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val encryptionService: EncryptionService
) : AccountDataSource {

    override fun getAll() = accountDao.getAll().map(this::toAccount)

    override fun getByEmail(email: String) = accountDao.getByEmail(email).let(this::toAccount)

    override fun getLastAccountActive() = accountDao.getLastAccountActive().let(this::toAccount)

    override fun add(account: Account) = try {
        accountDao.add(toAccountEntity(account))
    } catch (e: SQLiteConstraintException) {
        if (e.message?.contains("unique", true) == true) {
            throw AccountAlreadyExistsException()
        } else {
            throw e
        }
    }

    override fun update(account: Account) = accountDao.update(toAccountEntity(account))

    override fun remove(account: Account) = accountDao.remove(toAccountEntity(account))

    private fun toAccount(accountEntity: AccountEntity): Account = Account(
        username = accountEntity.username,
        password = encryptionService.decryptString(
            EncryptedDataAndIv(
                accountEntity.encryptedPassword,
                accountEntity.passwordEncryptionIv
            )
        ),
        name = accountEntity.name,
        email = accountEntity.email,
        photoData = accountEntity.photoData,
        lastActive = accountEntity.lastActive
    )

    private fun toAccountEntity(account: Account): AccountEntity =
        with(encryptionService.encrypt(account.password)) {
            return AccountEntity(
                username = account.username,
                encryptedPassword = encryptedData,
                passwordEncryptionIv = iv,
                name = account.name,
                email = account.email,
                photoData = account.photoData,
                lastActive = account.lastActive
            )
        }
}
