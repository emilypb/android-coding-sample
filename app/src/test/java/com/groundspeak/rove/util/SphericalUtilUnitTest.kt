package com.groundspeak.rove.util


import org.junit.Assert.assertEquals
import org.junit.Test

class SphericalUtilUnitTest {

    @Test
    fun computeHeading() {
        // test with a variety of positive and negative LatLngs
        var latLng1 = LatLng(40.76, -73.984)
        var latLng2 = LatLng(38.89, -77.032)
        assertEquals(-127.62, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(38.89, -77.032)
        assertEquals(-22.29, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        latLng1 = LatLng(40.76, -73.984)
        latLng2 = LatLng(38.89, 77.032)
        assertEquals(22.29, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(38.89, 77.032)
        assertEquals(127.62, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        latLng1 = LatLng(-40.76, 73.984)
        latLng2 = LatLng(-38.89, 77.032)
        assertEquals(52.36, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(-38.89, 77.032)
        assertEquals(177.59, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        latLng1 = LatLng(-40.76, 73.984)
        latLng2 = LatLng(38.89, 77.032)
        assertEquals(2.41, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        // test 0-related cases
        latLng1 = LatLng(0.0, 0.0)
        latLng2 = LatLng(38.89, 77.032)
        assertEquals(50.38, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(0.0, 0.0)
        assertEquals(-100.61, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        latLng1 = LatLng(0.0, 0.0)
        latLng2 = LatLng(0.0, 0.0)
        assertEquals(0.0, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)

        // test finding heading between same location
        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(40.76, 73.984)
        assertEquals(0.0, SphericalUtil.computeHeading(latLng1, latLng2), 0.1)
    }

    @Test
    fun computeDistanceBetween() {
        // test with a variety of positive and negative LatLngs
        var latLng1 = LatLng(40.76, -73.984)
        var latLng2 = LatLng(38.89, -77.032)
        assertEquals(
            333113.67, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )

        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(38.89, -77.032)
        assertEquals(
            1.07E7, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.01E7
        )

        latLng1 = LatLng(40.76, -73.984)
        latLng2 = LatLng(38.89, 77.032)
        assertEquals(
            1.07E7, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.01E7
        )

        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(38.89, 77.032)
        assertEquals(
            333113.67, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )

        latLng1 = LatLng(-40.76, 73.984)
        latLng2 = LatLng(-38.89, 77.032)
        assertEquals(
            333113.67, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )

        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(-38.89, 77.032)
        assertEquals(8862089.52, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )

        latLng1 = LatLng(-40.76, 73.984)
        latLng2 = LatLng(38.89, 77.032)
        assertEquals(
            8862089.52, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )

        // test 0-related cases
        latLng1 = LatLng(0.0, 0.0)
        latLng2 = LatLng(38.89, 77.032)
        assertEquals(8889010.71, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )

        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(0.0, 0.0)
        assertEquals(
            8666224.09, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )

        latLng1 = LatLng(0.0, 0.0)
        latLng2 = LatLng(0.0, 0.0)
        assertEquals(
            0.0, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )

        // test finding heading between same location
        latLng1 = LatLng(40.76, 73.984)
        latLng2 = LatLng(40.76, 73.984)
        assertEquals(
            0.0, SphericalUtil.computeDistanceBetween(latLng1, latLng2), 0.1
        )
    }
}
