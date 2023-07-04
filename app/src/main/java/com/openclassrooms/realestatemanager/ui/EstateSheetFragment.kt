package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate

class EstateSheetFragment(
    private val estateSheetFragmentListener: EstateSheetFragmentListener
    ) : Fragment() {

    interface EstateSheetFragmentListener {
        fun launchEstateEditFragment(estate: Estate)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_sheet_estate, container, false)
        val viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[EstateSheetFragmentViewModel::class.java]

        val statusTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_status)
        val descriptionTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_description)
        val surfaceTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_surface)
        val numbersOfRoomsTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_rooms)
        val numbersOfBathroomsTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_bathrooms)
        val numbersOfBedroomsTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_bedrooms)
        val locationTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_location)
        view.findViewById<ImageView>(R.id.fragment_sheet_estate_edit_button).setOnClickListener {
            estateSheetFragmentListener.launchEstateEditFragment(viewModel.getSelectedEstateLiveData().value ?: return@setOnClickListener )
        }

        viewModel.getSelectedEstateLiveData().observe(viewLifecycleOwner) { estate ->
            if (estate != null) {
                statusTextView.text = estate.status.getString(resources)
                descriptionTextView.text = estate.description
                surfaceTextView.text = estate.surface.toString()
                numbersOfRoomsTextView.text = estate.numberOfRooms.toString()
                numbersOfBathroomsTextView.text = estate.numberOfBathrooms.toString()
                numbersOfBedroomsTextView.text = estate.numberOfBedrooms.toString()
                locationTextView.text = viewModel.getLocationString(estate)
            }
        }

        return view
    }

}