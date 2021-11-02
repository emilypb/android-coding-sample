package com.groundspeak.rove.screens.navigate

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.groundspeak.rove.R
import com.groundspeak.rove.util.Util

class NavigateFragment : Fragment() {

    // viewmodel
    private lateinit var viewModel: NavigateViewModel

    // views
    private lateinit var arrow: ImageView
    private lateinit var distanceText: TextView
    private lateinit var status: TextView
    private lateinit var cancel: TextView

    // launcher for requesting location permissions
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.startLocationUpdates()
            } else {
                status.setText(R.string.location_error)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // create viewmodel with selected destination
        val viewModelFactory = NavigateViewModelFactory(
            requireActivity().application,
            requireArguments().getParcelable("secretDestination")
        )
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(NavigateViewModel::class.java)

        return inflater.inflate(R.layout.navigate_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrow = view.findViewById(R.id.compass_arrow)
        distanceText = view.findViewById(R.id.distance)
        status = view.findViewById(R.id.status_message)
        cancel = view.findViewById(R.id.cancel_action)

        // Set up observers for new location/direction data
        viewModel.currentLocation.observe(viewLifecycleOwner) {
            viewModel.updateLocationData()
        }
        viewModel.distanceToDest.observe(viewLifecycleOwner) {
            updateDistanceText(viewModel.distanceToDest.value)
        }
        viewModel.userDirection.observe(viewLifecycleOwner) {
            updateArrowDirection(viewModel.userDirection.value)
        }

        // get location permissions if necessary, but otherwise start updates
        status.setText(R.string.location_pending)
        when {
            hasLocationPermission() -> {
                viewModel.startLocationUpdates()
            }
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) ||
            shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) -> {
                // showInContextUI(...)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission_group.LOCATION)
            }
        }

        cancel.setOnClickListener {
            // cancel navigation and go back to primer fragment
            findNavController().navigate(R.id.action_return_to_beginning)
        }
    }

    private fun updateDistanceText(distance: Double?) {
        distance ?: return
        distanceText.text = Util.getDistanceString(distance)

        if (distance < viewModel.getRadius()) {
            val bundle = bundleOf("destinationMessage" to viewModel.getSecretMessage())
            findNavController().navigate(
                R.id.action_navigateFragment_to_destinationFragment, bundle)
        }
    }

    private fun updateArrowDirection(direction: Float?) {
        direction ?: return

        // update the arrow position with the new direction we've been given
        val matrix = Matrix()
        val bounds = arrow.drawable.bounds
        matrix.postRotate(
            direction, (bounds.width() / 2).toFloat(), (bounds.height() / 2).toFloat())
        arrow.scaleType = ImageView.ScaleType.MATRIX
        arrow.imageMatrix = matrix
    }

    private fun hasLocationPermission() : Boolean {
        activity ?: return false
        return  (ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

}