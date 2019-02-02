package org.visapps.vkstickerskeyboard.util

import android.view.View

fun toVisibility(constraint : Boolean): Int {
    return if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

