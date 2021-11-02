package com.groundspeak.rove.util

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilUnitTest {

    @Test
    fun getDistanceString() {
        // 0 test
        assertEquals("0m", Util.getDistanceString(0.0))

        // test rounding
        assertEquals("20m", Util.getDistanceString(19.6))

        assertEquals("20m", Util.getDistanceString(20.4))

        // test border of m/km cutoff with rounding
        assertEquals("0.5km", Util.getDistanceString(500.0))

        assertEquals("500m", Util.getDistanceString(499.8))

        assertEquals("0.5km", Util.getDistanceString(500.2))

        // test km rounding
        assertEquals("1.5km", Util.getDistanceString(1512.3))

        assertEquals("1.6km", Util.getDistanceString(1598.7))
    }
}