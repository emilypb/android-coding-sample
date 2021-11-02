package com.groundspeak.rove.screens.navigate

import android.app.Application
import androidx.lifecycle.*
import com.groundspeak.rove.models.Destination
import com.groundspeak.rove.models.LocationLiveData
import com.groundspeak.rove.util.Compass
import com.groundspeak.rove.util.LatLng
import com.groundspeak.rove.util.SphericalUtil

class NavigateViewModel(
    application: Application,
    private val secretDestination: Destination?
): AndroidViewModel(application), Compass.CompassListener {

    // data to help us calculate user position and where we want them to go
    private var compass = Compass(application, this)
    private var bearing: Float = DEFAULT_DIRECTION // direction to destination, relative to North
    private var heading: Float = DEFAULT_DIRECTION // direction device is pointing, relative to North

    // live data for the fragment to observe
    val distanceToDest = MutableLiveData(DEFAULT_DISTANCE)
    val currentLocation = LocationLiveData(application)
    val userDirection = MutableLiveData(DEFAULT_DIRECTION)

    fun startLocationUpdates() {
        currentLocation.startLocationUpdates()
        compass.start()
    }

    override fun onCleared() {
        currentLocation.stopLocationUpdates()
        compass.stop()
        super.onCleared()
    }

    override fun onHeadingUpdate(heading: Float) {
        this.heading = heading
        updateUserDirection()
    }

    private fun updateUserDirection() {
        userDirection.postValue(bearing - heading)
    }

    fun updateLocationData() {
        // quick null check
        currentLocation.value ?: return
        val targetLatLng = secretDestination?.let { LatLng(it.latitude, it.longitude) }
        targetLatLng ?: return

        // update the distance to destination
        val userLatLng = LatLng(currentLocation.value!!.latitude,
            currentLocation.value!!.longitude)
        distanceToDest.postValue(
            SphericalUtil.computeDistanceBetween(userLatLng, targetLatLng))

        // update which direction user is pointing and where they should be going
        bearing = SphericalUtil.computeHeading(userLatLng, targetLatLng).toFloat()
        updateUserDirection()
    }

    fun getSecretMessage() : String? {
        return secretDestination?.message
    }

    fun getRadius() : Int {
        return secretDestination?.radius ?: DEFAULT_RADIUS
    }

    companion object {
        private const val DEFAULT_RADIUS = 5
        private const val DEFAULT_DISTANCE = 100.0
        private const val DEFAULT_DIRECTION = 0.0F
    }
}