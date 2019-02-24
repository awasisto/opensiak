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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val username: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val encryptedPassword: ByteArray,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val passwordEncryptionIv: ByteArray,
    val name: String,
    val email: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val photoData: ByteArray,
    val lastActive: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AccountEntity
        if (username != other.username) return false
        if (!encryptedPassword.contentEquals(other.encryptedPassword)) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (!photoData.contentEquals(other.photoData)) return false
        if (lastActive != other.lastActive) return false
        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + encryptedPassword.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + photoData.contentHashCode()
        result = 31 * result + lastActive.hashCode()
        return result
    }
}