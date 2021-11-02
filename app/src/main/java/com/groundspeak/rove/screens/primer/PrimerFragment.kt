package com.groundspeak.rove.screens.primer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.groundspeak.rove.R

class PrimerFragment : Fragment() {

    // viewmodel
    private val viewModel: PrimerViewModel by viewModels()

    // views
    private lateinit var location1Button: Button
    private lateinit var location2Button: Button
    private lateinit var message: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.primer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location1Button = view.findViewById(R.id.button_location_1)
        location2Button = view.findViewById(R.id.button_location_2)
        message = view.findViewById(R.id.textView)

        message.setText(R.string.secret_location_loading)

        // update buttons and set click actions when destination data is loaded
        viewModel.secretDestination1.observe(viewLifecycleOwner) {
            message.setText(R.string.secret_location_received)
            location1Button.isEnabled = true
            location1Button.setOnClickListener {
                val bundle = bundleOf("secretDestination" to viewModel.secretDestination1.value)
                findNavController().navigate(
                    R.id.action_primerFragment_to_navigateFragment, bundle)
            }
        }
        viewModel.secretDestination2.observe(viewLifecycleOwner) {
            location2Button.isEnabled = true
            location2Button.setOnClickListener {
                val bundle = bundleOf("secretDestination" to viewModel.secretDestination2.value)
                findNavController().navigate(
                    R.id.action_primerFragment_to_navigateFragment, bundle)
            }
        }
    }

}