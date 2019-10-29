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

package com.wasisto.opensiak.data.siakng.pagescraper

import com.wasisto.opensiak.data.siakng.AuthenticationFailedException
import com.wasisto.opensiak.model.Credentials
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.jsoup.Jsoup
import java.net.CookieManager

class SiakHttpClient private constructor(
    private val credentials: Credentials
) {
    companion object {
        private val instances = mutableMapOf<Credentials, SiakHttpClient>()

        @Synchronized
        fun get(credentials: Credentials): SiakHttpClient {
            var instance = instances[credentials]
            if (instance == null) {
                instance = SiakHttpClient(credentials)
                instances[credentials] = instance
            }
            return instance
        }
    }

    private val okHttpClientInd = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()

    private val okHttpClientEng = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()

    private fun unauthenticatedHttpGet(url: String, okHttpClient: OkHttpClient): Response {
        return okHttpClient.newCall(Request.Builder().url(url).build()).execute()
    }

    private fun unauthenticatedHttpGet(url: String): BilingualResponse {
        return runBlocking {
            val deferredResponseInd = async { unauthenticatedHttpGet(url, okHttpClientInd) }
            val deferredResponseEng = async { unauthenticatedHttpGet(url, okHttpClientEng) }
            BilingualResponse(
                responseInd = deferredResponseInd.await(),
                responseEng = deferredResponseEng.await()
            )
        }
    }

    private fun isRedirectedToAuthenticationPage(response: Response): Boolean {
        return if ("text/html" == response.header("Content-Type")) {
            val document = Jsoup.parse(response.peekBody(java.lang.Long.MAX_VALUE).string())
            val loginElement = document.select("#login").first()
            loginElement != null
        } else {
            false
        }
    }

    private fun isAuthenticationSuccess(authenticationResponse: Response): Boolean {
        val authenticationDocument = Jsoup.parse(authenticationResponse.body()!!.string())
        val errorElement = authenticationDocument.select("#login > p").first()
        return errorElement == null
    }

    private fun changeRole(okHttpClient: OkHttpClient) {
        unauthenticatedHttpGet("https://academic.ui.ac.id/main/Authentication/ChangeRole", okHttpClient)
    }

    private fun authenticate() {
        val authenticationRequest = Request.Builder()
            .url("https://academic.ui.ac.id/main/Authentication/Index")
            .post(
                FormBody.Builder()
                    .add("u", credentials.username)
                    .add("p", credentials.password)
                    .build()
            )
            .build()

        runBlocking {
            val deferredAuthenticateInd = async {
                val authenticationResponse = okHttpClientInd.newCall(authenticationRequest).execute()

                if (isAuthenticationSuccess(authenticationResponse)) {
                    changeRole(okHttpClientInd)
                    unauthenticatedHttpGet("https://academic.ui.ac.id/main/System/Language?lang=id", okHttpClientInd)
                } else {
                    throw AuthenticationFailedException()
                }
            }

            val deferredAuthenticateEng = async {
                val authenticationResponse = okHttpClientEng.newCall(authenticationRequest).execute()

                if (isAuthenticationSuccess(authenticationResponse)) {
                    changeRole(okHttpClientEng)
                    unauthenticatedHttpGet("https://academic.ui.ac.id/main/System/Language?lang=en", okHttpClientEng)
                } else {
                    throw AuthenticationFailedException()
                }
            }

            deferredAuthenticateInd.await()
            deferredAuthenticateEng.await()
        }
    }

    fun httpGet(url: String): BilingualResponse {
        val bilingualResponse = unauthenticatedHttpGet(url)

        return if (isRedirectedToAuthenticationPage(bilingualResponse.responseInd)
                || isRedirectedToAuthenticationPage(bilingualResponse.responseEng)) {
            authenticate()
            unauthenticatedHttpGet(url)
        } else {
            bilingualResponse
        }
    }

    data class BilingualResponse(
        val responseInd: Response,
        val responseEng: Response
    )
}