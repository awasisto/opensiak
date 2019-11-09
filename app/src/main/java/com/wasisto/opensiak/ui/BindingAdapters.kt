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

package com.wasisto.opensiak.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.TextView
import com.wasisto.opensiak.R

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("refreshingWhen")
fun refreshingWhen(swipeRefreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
    swipeRefreshLayout.isRefreshing = refreshing
}

@BindingAdapter("photoData")
fun photoData(imageView: ImageView, photoData: ByteArray?) {
    if (photoData != null) {
        val bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.size)
        if (bitmap != null) {
            imageView.setImageBitmap(
                if (bitmap.width > bitmap.height) {
                    Bitmap.createBitmap(bitmap, bitmap.width / 2 - bitmap.height / 2, 0, bitmap.height, bitmap.height)
                } else {
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.width)
                }
            )
        } else {
            imageView.setImageResource(R.drawable.empty_student_photo)
        }
    } else {
        imageView.setImageResource(R.drawable.empty_student_photo)
    }
}

@BindingAdapter("textResId")
fun textResId(textView: TextView, textResId: Int) {
    textView.setText(textResId)
}

@BindingAdapter("flipVerticallyWhen")
fun flipVerticallyWhen(view: View, flipVertically: Boolean) {
    view.scaleY = if (flipVertically) -1f else 1f
}
