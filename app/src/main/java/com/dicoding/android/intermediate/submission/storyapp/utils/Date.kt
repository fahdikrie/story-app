package com.dicoding.android.intermediate.submission.storyapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun String.withDateFormat(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("HH:mm dd/MM/yyyy ")
    return formatter.format(parser.parse(this) as Date)
}
