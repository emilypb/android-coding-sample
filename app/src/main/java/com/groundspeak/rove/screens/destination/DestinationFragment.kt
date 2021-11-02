package com.groundspeak.rove.screens.destination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.groundspeak.rove.R

class DestinationFragment : Fragment() {

    private var message: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        message = requireArguments().getString("destinationMessage", "")
        return inflater.inflate(R.layout.destination_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // show destination message
        val messageView: TextView = view.findViewById(R.id.destination_message)
        messageView.text = message

        // set up done button to return to primer fragment
        val doneButton: Button = view.findViewById(R.id.button_done)
        doneButton.setOnClickListener {
            findNavController().navigate(R.id.action_return_to_beginning)
        }
    }
}