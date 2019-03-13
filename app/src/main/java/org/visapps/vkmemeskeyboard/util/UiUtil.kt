package org.visapps.vkmemeskeyboard.util

import android.content.Context
import android.view.View

fun calculateNoOfColumns(context: Context): Int {
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / 120).toInt()
}


fun toVisibility(constraint : Boolean): Int {
    return if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun switchVisibility(constraint : Boolean): Int {
    return if (constraint) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}