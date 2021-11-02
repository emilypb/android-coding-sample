package com.groundspeak.rove.screens.navigate

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.groundspeak.rove.models.Destination

class NavigateViewModelFactory(
    private val application: Application,
    private val secretDestination: Destination?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NavigateViewModel::class.java)) {
            return NavigateViewModel(application, secretDestination) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}