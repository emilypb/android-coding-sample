package com.groundspeak.rove.util

import java.util.*

object Util {
    private const val METRE_CUTOFF = 500
    fun getDistanceString(meters: Double): String {
        return if (meters < METRE_CUTOFF) {
            String.format(Locale.getDefault(), "%.0fm", meters)
        } else {
            val km = meters / 1000
            String.format(Locale.getDefault(), "%.1fkm", km)
        }
    }
}