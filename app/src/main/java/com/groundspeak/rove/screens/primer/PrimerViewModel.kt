package com.groundspeak.rove.screens.primer

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groundspeak.rove.models.Destination
import com.groundspeak.rove.network.DestinationInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class PrimerViewModel : ViewModel() {

    private lateinit var destinations: List<Destination>
    private val destinationCallback: Callback<List<Destination>> = object:
        Callback<List<Destination>> {
        override fun onResponse(
            call: Call<List<Destination>>,
            response: Response<List<Destination>>
        ) {
            if (response.body() != null) {
                onDestinationsReceived(response.body()!!)
            }
        }

        override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
            onDestinationCallError(t)
        }
    }

    var secretDestination1 = MutableLiveData<Destination>()
    var secretDestination2 = MutableLiveData<Destination>()

    init {
        val destinationInterface = DestinationInterface.create().getDestinations()
        destinationInterface.enqueue(destinationCallback)
    }

    @VisibleForTesting
    fun onDestinationsReceived(destinations: List<Destination>) {
        this.destinations = destinations
        if (destinations.isNotEmpty()) {
            val shuffledDestinations = destinations.shuffled()
            secretDestination1.postValue(shuffledDestinations.first())
            secretDestination2.postValue(shuffledDestinations.last())
        }
    }

    private fun onDestinationCallError(t: Throwable) {
        // print the error and show the user an error message
        println("error: ${t.message}")
    }
}