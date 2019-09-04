OpenSIAK
========

[![Build Status](https://travis-ci.com/awasisto/opensiak.svg?branch=master)](https://travis-ci.com/awasisto/opensiak)

An open source University of Indonesia SIAK-NG Android app.

https://play.google.com/store/apps/details?id=com.wasisto.opensiak

Screenshots
-----------

![OpenSIAK screenshot 1](https://lh3.googleusercontent.com/nA9OICiZZ17jZO5i_YBrtELvKIIZztFUYNW4MJqKnQ8fIJTVJVdabFCh9-DEOrM81g=w250-rw)
![OpenSIAK screenshot 2](https://lh3.googleusercontent.com/l5e4zvcRLP3AVuXUqWnkVz5G1OW3kHEQrYJP_rI_rTEfcWAtxjGVctK8lRuEjYI4lw=w250-rw)

Architecture
------------

The architecture is built around the Android Architecture Components with Dagger 2 for dependency
injection.

The presentation logic is kept away from Activities and Fragments and moved to the viewmodels. The
data are observed using LiveData Data Binding Library is used to bind UI components in layouts
to the app's data sources.

A Repository layer is implemented for handling data operations. The data come from different
sources - student data are scraped from the SIAK-NG website using jsoup library, user preferences
and settings are stored in a local database.

A lightweight domain layer is implemented between the data layer and the presentation layer to
handle discrete pieces of business logic off the UI thread.

License
-------

    Copyright (C) 2019 Andika Wasisto

    OpenSIAK is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenSIAK is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenSIAK.  If not, see <https://www.gnu.org/licenses/>.