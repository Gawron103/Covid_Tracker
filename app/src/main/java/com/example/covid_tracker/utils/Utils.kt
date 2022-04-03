package com.example.covid_tracker.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(view: View, message: String) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snackBar.show()
}