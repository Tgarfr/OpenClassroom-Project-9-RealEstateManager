package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate
import java.util.Calendar

class EstateEditFragment(
    private val setting: Setting,
    private val estateEditFragmentListener: EstateEditFragmentListener
    ) : Fragment() {

    enum class Setting { ADD, EDIT }
    private lateinit var viewModel: EstateEditFragmentViewModel
    private var type: Estate.Type? = null
    private lateinit var spinner: Spinner
    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var surfaceEditText: EditText
    private lateinit var numberOfRoomsEditText: EditText
    private lateinit var numberOfBathroomsEditText: EditText
    private lateinit var numberOfBedroomsEditText: EditText
    private lateinit var houseNumberEditText: EditText
    private lateinit var streetEditText: EditText
    private lateinit var additionalAddressEditText: EditText
    private lateinit var zipCodeEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var button: Button

    interface EstateEditFragmentListener {
        fun launchEstateSheetFragment(estate: Estate)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_edit_estate, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[EstateEditFragmentViewModel::class.java]
        initViews(view)

        if (setting == Setting.EDIT) {
            viewModel.getSelectedEstateLiveData().observe(viewLifecycleOwner){ estate -> injectEstateToViews(estate) }
        }

        val adapter = EstateTypeSpinnerAdapter(resources)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = onSelectedType

        button.setOnClickListener(clickOnValidButton)

        return view
    }

    private fun initViews(view: View) {
        priceEditText = view.findViewById(R.id.fragment_edit_estate_price)
        descriptionEditText = view.findViewById(R.id.fragment_edit_estate_description)
        surfaceEditText = view.findViewById(R.id.fragment_edit_estate_surface)
        numberOfRoomsEditText = view.findViewById(R.id.fragment_edit_estate_number_of_rooms)
        numberOfBathroomsEditText = view.findViewById(R.id.fragment_edit_estate_number_of_bathrooms)
        numberOfBedroomsEditText = view.findViewById(R.id.fragment_edit_estate_number_of_bedrooms)
        houseNumberEditText = view.findViewById(R.id.fragment_edit_estate_house_number)
        streetEditText = view.findViewById(R.id.fragment_edit_estate_street)
        additionalAddressEditText = view.findViewById(R.id.fragment_edit_estate_additional_address)
        zipCodeEditText = view.findViewById(R.id.fragment_edit_estate_zipCode)
        cityEditText = view.findViewById(R.id.fragment_edit_estate_city)
        countryEditText = view.findViewById(R.id.fragment_edit_estate_country)
        spinner = view.findViewById(R.id.fragment_edit_estate_spinner_type)
        button = view.findViewById(R.id.fragment_edit_estate_button_add)
        if (setting == Setting.ADD) {
            button.text = resources.getString(R.string.edit_estate_add)
        } else {
            button.text = resources.getString(R.string.edit_estate_edit)
        }
    }

    private fun injectEstateToViews(estate: Estate) {
        spinner.setSelection(estate.type.ordinal + 1)
        priceEditText.setText(estate.price.toString())
        descriptionEditText.setText(estate.description)
        surfaceEditText.setText(estate.surface.toString())
        numberOfRoomsEditText.setText(estate.numberOfRooms.toString())
        numberOfBathroomsEditText.setText(estate.numberOfBathrooms.toString())
        numberOfBedroomsEditText.setText(estate.numberOfBedrooms.toString())
        houseNumberEditText.setText(estate.houseNumber.toString())
        streetEditText.setText(estate.street)
        additionalAddressEditText.setText(estate.additionalAddress)
        zipCodeEditText.setText(estate.zipCode)
        cityEditText.setText(estate.city)
        countryEditText.setText(estate.country)
    }

    private val clickOnValidButton = OnClickListener {

        if (formIsCompleted()) {

            val id = if (setting == Setting.ADD) {
                System.currentTimeMillis()
            } else {
                viewModel.getSelectedEstateLiveData().value?.id ?: return@OnClickListener
            }

            val estate = Estate(
                id = id,
                type = this.type ?: return@OnClickListener,
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
                entryDate = Calendar.getInstance().timeInMillis,
                saleDate = null,
                agent = "Agent"
            )

            if (setting == Setting.ADD) {
                viewModel.addEstate(estate)
                Toast.makeText(activity, resources.getString(R.string.edit_estate_added), Toast.LENGTH_LONG).show()
            } else {
                viewModel.updateEstate(estate)
                Toast.makeText(activity, resources.getString(R.string.edit_estate_edited), Toast.LENGTH_LONG).show()
            }
            viewModel.updateEstate(estate)

            estateEditFragmentListener.launchEstateSheetFragment(estate)
        }
    }

    private val onSelectedType = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            type = if (position == 0) { null }
            else {
                Estate.Type.values()[position - 1]
            }
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    private fun formIsCompleted(): Boolean {
        when {
            this.type == null -> Toast.makeText(activity, resources.getString(R.string.edit_estate_type_required), Toast.LENGTH_SHORT).show()
            priceEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_price_required), Toast.LENGTH_SHORT).show()
            descriptionEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_description_required), Toast.LENGTH_SHORT).show()
            surfaceEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_surface_required), Toast.LENGTH_SHORT).show()
            numberOfRoomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_numberOfRooms_required), Toast.LENGTH_SHORT).show()
            numberOfBathroomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_numberOfBathrooms_required), Toast.LENGTH_SHORT).show()
            numberOfBedroomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_numberOfBedrooms_required), Toast.LENGTH_SHORT).show()
            houseNumberEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_houseNumber_required), Toast.LENGTH_SHORT).show()
            streetEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_street_required), Toast.LENGTH_SHORT).show()
            zipCodeEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_zipCode_required), Toast.LENGTH_SHORT).show()
            cityEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_city_required), Toast.LENGTH_SHORT).show()
            countryEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_country_required), Toast.LENGTH_SHORT).show()
            else -> return true
        }
        return false
    }

}