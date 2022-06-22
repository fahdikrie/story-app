package com.dicoding.android.intermediate.submission.storyapp.utils

import android.content.Context
import android.location.Geocoder
import java.io.IOException
import java.util.*

fun getAddressName(ctx: Context, lat: Double, lon: Double): String? {
    var addressName: String? = null
    val geocoder = Geocoder(ctx, Locale.getDefault())
    try {
        val list = geocoder.getFromLocation(lat, lon, 1)
        if (list != null && list.size != 0) {
            addressName = list[0].adminArea
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return addressName
}
