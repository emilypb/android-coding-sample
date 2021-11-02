package com.groundspeak.rove.util

import android.content.Context
import android.hardware.*
import android.location.Location
import android.view.Surface
import com.groundspeak.rove.util.Compass.CompassListener
import com.groundspeak.rove.util.Compass
import android.view.WindowManager

class Compass(private val context: Context, private val listener: CompassListener) {
    // Heading = direction device is pointing, relative to North (clockwise)
    // Bearing = direction to destination, relative to North (clockwise)
    // Relative bearing = angle between Heading and Bearing (clockwise) (i.e. Bearing minus Heading)
    interface CompassListener {
        /**
         * @param heading Heading in degrees, clockwise from North
         */
        fun onHeadingUpdate(heading: Float)
    }

    private var mGravityVector = FloatArray(3)
    private var mMagneticFieldVector = FloatArray(3)
    private val mOrientation = FloatArray(3)
    private val mRotationMatrix = FloatArray(9)
    private val mSensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> mGravityVector = lowPass(event.values.clone(), mGravityVector)
                Sensor.TYPE_MAGNETIC_FIELD -> mMagneticFieldVector = lowPass(event.values.clone(), mMagneticFieldVector)
            }
            if (SensorManager.getRotationMatrix(mRotationMatrix, null, mGravityVector, mMagneticFieldVector)) {
                val rotation = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
                var x = SensorManager.AXIS_X
                var y = SensorManager.AXIS_Y
                when (rotation) {
                    Surface.ROTATION_90 -> {
                        x = SensorManager.AXIS_Y
                        y = SensorManager.AXIS_MINUS_X
                    }
                    Surface.ROTATION_180 -> {
                        x = SensorManager.AXIS_MINUS_X
                        y = SensorManager.AXIS_MINUS_Y
                    }
                    Surface.ROTATION_270 -> {
                        x = SensorManager.AXIS_MINUS_Y
                        y = SensorManager.AXIS_X
                    }
                }
                val fixedRotationMatrix = FloatArray(9)
                SensorManager.remapCoordinateSystem(mRotationMatrix, x, y, fixedRotationMatrix)
                SensorManager.getOrientation(fixedRotationMatrix, mOrientation)
                listener.onHeadingUpdate((180 * mOrientation[0] / Math.PI).toFloat())
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    fun start() {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER))
        sensorManager.unregisterListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD))
    }

    companion object {
        private const val LOW_PASS_ALPHA = 0.15f

        /**
         * Adjusts heading to account for variance between magnetic and polar/"True" north
         */
        fun adjustHeadingForDeclination(heading: Float, location: Location?): Float {
            var declination = 0f
            if (location != null) {
                val field = GeomagneticField(location.latitude.toFloat(), location.longitude.toFloat(), location.altitude.toFloat(), System.currentTimeMillis())
                declination = field.declination
            }
            return heading + declination
        }

        private fun lowPass(input: FloatArray, output: FloatArray?): FloatArray {
            if (output == null) return input
            for (i in 0 until input.size.coerceAtMost(output.size)) {
                output[i] = output[i] + LOW_PASS_ALPHA * (input[i] - output[i])
            }
            return output
        }
    }
}