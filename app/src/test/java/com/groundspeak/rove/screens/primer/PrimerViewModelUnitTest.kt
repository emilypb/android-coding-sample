package com.groundspeak.rove.screens.primer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.groundspeak.rove.models.Destination
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

private val TEST_DESTINATIONS = listOf(
    Destination(1, "1", 0.0, 0.0, 1, "1"),
    Destination(2, "2", 0.0, 0.0, 2, "2"),
    Destination(3, "3", 0.0, 0.0, 3, "3"),
    Destination(4, "4", 0.0, 0.0, 4, "4"),
    Destination(5, "5", 0.0, 0.0, 5, "5")
)

class PrimerViewModelUnitTest {

    @Rule
    @JvmField val rule = InstantTaskExecutorRule()

    @Test
    fun onDestinationsReceived() {
        val viewModel = PrimerViewModel()
        viewModel.onDestinationsReceived(TEST_DESTINATIONS)

        assertNotNull("Destination 1 should not be null",
            viewModel.secretDestination1.value)
        assertNotNull("Destination 2 should not be null",
            viewModel.secretDestination2.value)

        val destination1 = viewModel.secretDestination1.value!!
        val destination2 = viewModel.secretDestination2.value!!

        assertNotSame(
            "Secret destinations should not be the same",
            destination1, destination2
        )
        assertTrue(
            "Destination 1 should be in sample data",
            TEST_DESTINATIONS.contains(destination1)
        )
        assertTrue(
            "Destination 2 should be in sample data",
            TEST_DESTINATIONS.contains(destination2)
        )
    }

    @Test
    fun emptyDestinationsTest() {
        val viewModel = PrimerViewModel()
        viewModel.onDestinationsReceived(emptyList())
        assertNull("Destination 1 should be null",
            viewModel.secretDestination1.value)
        assertNull("Destination 2 should be null",
            viewModel.secretDestination2.value)
    }
}