package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate
import java.util.*

class EstateAddFragment(private val estateAddFragmentListener: EstateAddFragmentListener) : Fragment() {

    private var type: Estate.Type? = null

    interface EstateAddFragmentListener {
        fun launchEstateSheetFragment(estate: Estate)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_add_estate, container, false)
        val viewModel = ViewModelProvider(this, ViewModelFactory)[EstateAddFragmentViewModel::class.java]

        val priceEditText: EditText = view.findViewById(R.id.fragment_add_estate_price)
        val descriptionEditText: EditText = view.findViewById(R.id.fragment_add_estate_description)
        val surfaceEditText: EditText = view.findViewById(R.id.fragment_add_estate_surface)
        val numberOfRoomsEditText: EditText = view.findViewById(R.id.fragment_add_estate_number_of_rooms)
        val numberOfBathroomsEditText: EditText = view.findViewById(R.id.fragment_add_estate_number_of_bathrooms)
        val numberOfBedroomsEditText: EditText = view.findViewById(R.id.fragment_add_estate_number_of_bedrooms)
        val houseNumberEditText: EditText = view.findViewById(R.id.fragment_add_estate_house_number)
        val streetEditText: EditText = view.findViewById(R.id.fragment_add_estate_street)
        val additionalAddressEditText: EditText = view.findViewById(R.id.fragment_add_estate_additional_address)
        val zipCodeEditText: EditText = view.findViewById(R.id.fragment_add_estate_zipCode)
        val cityEditText: EditText = view.findViewById(R.id.fragment_add_estate_city)
        val countryEditText: EditText = view.findViewById(R.id.fragment_add_estate_country)
        val spinner: Spinner = view.findViewById(R.id.fragment_add_estate_spinner_type)

        val adapter = EstateTypeSpinnerAdapter(resources)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = onSelectedType

        val addButton: Button = view.findViewById(R.id.fragment_add_estate_button_add)
        addButton.setOnClickListener {
            val type = this.type
            when {
                type == null -> Toast.makeText(activity, resources.getString(R.string.add_estate_type_required), Toast.LENGTH_SHORT).show()
                priceEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_price_required), Toast.LENGTH_SHORT).show()
                descriptionEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_description_required), Toast.LENGTH_SHORT).show()
                surfaceEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_surface_required), Toast.LENGTH_SHORT).show()
                numberOfRoomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_numberOfRooms_required), Toast.LENGTH_SHORT).show()
                numberOfBathroomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_numberOfBathrooms_required), Toast.LENGTH_SHORT).show()
                numberOfBedroomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_numberOfBedrooms_required), Toast.LENGTH_SHORT).show()
                houseNumberEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_houseNumber_required), Toast.LENGTH_SHORT).show()
                streetEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_street_required), Toast.LENGTH_SHORT).show()
                zipCodeEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_zipCode_required), Toast.LENGTH_SHORT).show()
                cityEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_city_required), Toast.LENGTH_SHORT).show()
                countryEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.add_estate_country_required), Toast.LENGTH_SHORT).show()
                else -> {
                    val estate = Estate(
                        id = System.currentTimeMillis(),
                        type = type,
                        price = priceEditText.text.toString().toDouble(),
                        surface = surfaceEditText.text.toString().toFloat(),
                        numberOfRooms = numberOfRoomsEditText.text.toString().toInt(),
                        numberOfBathrooms = numberOfBathroomsEditText.text.toString().toInt(),
                        numberOfBedrooms = numberOfBedroomsEditText.text.toString().toInt(),
                        description = descriptionEditText.text.toString(),
                        houseNumber = houseNumberEditText.text.toString().toInt(),
                        street = streetEditText.text.toString(),
                        additionalAddress = additionalAddressEditText.text.toString(),
                        zipCode = zipCodeEditText.text.toString(),
                        city =  cityEditText.text.toString(),
                        country = countryEditText.text.toString(),
                        status = Estate.Status.AVAILABLE,
                        entryDate = Calendar.getInstance(),
                        saleDate = null,
                        agent = "Agent"
                    )
                    viewModel.addEstate(estate)
                    Toast.makeText(activity, resources.getString(R.string.add_estate_added), Toast.LENGTH_LONG).show()
                    estateAddFragmentListener.launchEstateSheetFragment(estate)
                }
            }
        }
        return view
    }

    private val onSelectedType = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            type = if (position == 0) { null }
            else {
                Estate.Type.values()[position]
            }
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

}