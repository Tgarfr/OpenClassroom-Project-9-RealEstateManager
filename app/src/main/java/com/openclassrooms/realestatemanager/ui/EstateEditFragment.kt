package com.openclassrooms.realestatemanager.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import java.util.Calendar

class EstateEditFragment(
    private val setting: Setting,
    private val estateEditFragmentListener: EstateEditFragmentListener
    ) : Fragment() {

    enum class Setting { ADD, EDIT }
    private lateinit var viewModel: EstateEditFragmentViewModel
    private lateinit var picturesAdapter: EstatePicturesAdapter
    private var id: Long? = null
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
        initData(view)
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
    }

    private fun initData(view: View) {
        when (setting) {
            Setting.ADD -> {
                button.text = resources.getString(R.string.edit_estate_add)
                viewModel.setNewEditedEstate()
                this.id = System.currentTimeMillis()
                initPictures(view)
            }
            Setting.EDIT -> {
                button.text = resources.getString(R.string.edit_estate_edit)
                viewModel.getEditedEstateLiveData().observe(viewLifecycleOwner){ estate ->
                    this.id = estate.id
                    injectEstateToViews(estate)
                    initPictures(view)
                }
            }
        }

        val typesAdapter = EstateTypeSpinnerAdapter(resources)
        spinner.adapter = typesAdapter
        spinner.onItemSelectedListener = onSelectedType

        button.setOnClickListener(clickOnValidButton)
    }

    private fun initPictures (view: View)  {
        view.findViewById<ImageView>(R.id.fragment_edit_estate_button_add_pictures).setOnClickListener {
            registerForAddPicturesButtonResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        picturesAdapter = EstatePicturesAdapter(requireContext(), null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_sheet_estate_pictures_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = picturesAdapter
        viewModel.getPictureListLiveData(this.id?: return).observe(viewLifecycleOwner) { pictureList -> picturesAdapter.submitList(pictureList) }
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

            val houseNumber = houseNumberEditText.text.toString().toInt()
            val street = streetEditText.text.toString()
            val zipCode = zipCodeEditText.text.toString()
            val city = cityEditText.text.toString()
            val country = countryEditText.text.toString()
            val position = viewModel.computeAddress("$houseNumber $street, $zipCode $city, $country", requireContext())

            val estate = Estate(
                id = this.id ?: return@OnClickListener,
                type = this.type ?: return@OnClickListener,
                price = priceEditText.text.toString().toDouble(),
                surface = surfaceEditText.text.toString().toFloat(),
                numberOfRooms = numberOfRoomsEditText.text.toString().toInt(),
                numberOfBathrooms = numberOfBathroomsEditText.text.toString().toInt(),
                numberOfBedrooms = numberOfBedroomsEditText.text.toString().toInt(),
                description = descriptionEditText.text.toString(),
                houseNumber = houseNumber,
                street = street,
                additionalAddress = additionalAddressEditText.text.toString(),
                zipCode = zipCode,
                city = city,
                country = country,
                latitude = position.latitude,
                longitude = position.longitude,
                status = Estate.Status.AVAILABLE,
                entryDate = Calendar.getInstance().timeInMillis,
                saleDate = null,
                agent = "Agent"
            )

            when (setting) {
                Setting.ADD -> {
                    viewModel.addEstate(estate)
                    Toast.makeText(activity, resources.getString(R.string.edit_estate_added), Toast.LENGTH_LONG).show()
                }
                Setting.EDIT -> {
                    viewModel.updateEstate(estate)
                    Toast.makeText(activity, resources.getString(R.string.edit_estate_edited), Toast.LENGTH_LONG).show()
                }
            }

            estateEditFragmentListener.launchEstateSheetFragment(estate)
        }
    }

    private fun formIsCompleted(): Boolean {
        when {
            this.type == null -> Toast.makeText(activity, resources.getString(R.string.edit_estate_type_required), Toast.LENGTH_SHORT).show()
            picturesAdapter.currentList.isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_picture_required), Toast.LENGTH_SHORT).show()
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

    private val registerForAddPicturesButtonResult = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())

                alertDialogBuilder.setTitle(resources.getString(R.string.picture_description_write))

                val editText = EditText(context)
                editText.inputType = InputType.TYPE_CLASS_TEXT
                alertDialogBuilder.setView(editText)

                alertDialogBuilder.setPositiveButton(resources.getString(R.string.picture_button_ok), object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (editText.text.isEmpty()) {
                           Toast.makeText(activity, resources.getString(R.string.picture_description_required), Toast.LENGTH_SHORT).show()
                        } else {
                            val picture = Picture(System.currentTimeMillis(), id ?: return, uris[0],editText.text.toString())
                            viewModel.addPicture(picture)
                            Toast.makeText(activity, resources.getString(R.string.picture_added), Toast.LENGTH_SHORT).show()
                        }
                    }
                })

                alertDialogBuilder.show()
            }
        }

}