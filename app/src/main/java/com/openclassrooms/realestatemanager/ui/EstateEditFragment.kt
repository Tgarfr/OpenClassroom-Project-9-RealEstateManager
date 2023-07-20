package com.openclassrooms.realestatemanager.ui

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EstateEditFragment(
    private val setting: Setting,
    private val estateEditFragmentListener: EstateEditFragmentListener
    ) : Fragment() {

    enum class Setting { ADD, EDIT }
    private lateinit var viewModel: EstateEditFragmentViewModel
    private lateinit var picturesAdapter: EstatePicturesAdapter
    private var id: Long? = null
    private var entryDate: Long? = null
    private var status = Estate.Status.AVAILABLE
    private var type: Estate.Type? = null
    private var agent: Agent? = null
    private val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    private lateinit var entryDateButton: Button
    private lateinit var typeSpinner: Spinner
    private lateinit var agentSpinner: Spinner
    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var surfaceEditText: EditText
    private lateinit var numberOfRoomsEditText: EditText
    private lateinit var numberOfBathroomsEditText: EditText
    private lateinit var numberOfBedroomsEditText: EditText
    private lateinit var schoolDistanceEditText: EditText
    private lateinit var shopDistanceEditText: EditText
    private lateinit var parkDistanceEditText: EditText
    private lateinit var houseNumberEditText: EditText
    private lateinit var streetEditText: EditText
    private lateinit var additionalAddressEditText: EditText
    private lateinit var zipCodeEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var validationButton: Button

    interface EstateEditFragmentListener {
        fun launchEstateSheetFragment(estate: Estate)
    }

    companion object {
        private const val CACHE_PICTURE_NAME = "cachePicture.jpg"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_edit_estate, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[EstateEditFragmentViewModel::class.java]
        initViews(view)
        initData(view)
        return view
    }

    private fun initViews(view: View) {
        entryDateButton = view.findViewById(R.id.fragment_edit_estate_button_entry_date)
        typeSpinner = view.findViewById(R.id.fragment_edit_estate_spinner_type)
        agentSpinner = view.findViewById(R.id.fragment_edit_estate_spinner_agent)
        priceEditText = view.findViewById(R.id.fragment_edit_estate_price)
        descriptionEditText = view.findViewById(R.id.fragment_edit_estate_description)
        surfaceEditText = view.findViewById(R.id.fragment_edit_estate_surface)
        numberOfRoomsEditText = view.findViewById(R.id.fragment_edit_estate_number_of_rooms)
        numberOfBathroomsEditText = view.findViewById(R.id.fragment_edit_estate_number_of_bathrooms)
        numberOfBedroomsEditText = view.findViewById(R.id.fragment_edit_estate_number_of_bedrooms)
        schoolDistanceEditText = view.findViewById(R.id.fragment_sheet_estate_distance_school)
        shopDistanceEditText = view.findViewById(R.id.fragment_sheet_estate_distance_shop)
        parkDistanceEditText = view.findViewById(R.id.fragment_sheet_estate_distance_park)
        houseNumberEditText = view.findViewById(R.id.fragment_edit_estate_house_number)
        streetEditText = view.findViewById(R.id.fragment_edit_estate_street)
        additionalAddressEditText = view.findViewById(R.id.fragment_edit_estate_additional_address)
        zipCodeEditText = view.findViewById(R.id.fragment_edit_estate_zipCode)
        cityEditText = view.findViewById(R.id.fragment_edit_estate_city)
        countryEditText = view.findViewById(R.id.fragment_edit_estate_country)
        validationButton = view.findViewById(R.id.fragment_edit_estate_button_validation)
    }

    private fun initData(view: View) {
        when (setting) {
            Setting.ADD -> {
                validationButton.text = resources.getString(R.string.edit_estate_add)
                viewModel.setNewEditedEstate()
                this.id = System.currentTimeMillis()
                initPictures(view)
            }
            Setting.EDIT -> {
                validationButton.text = resources.getString(R.string.edit_estate_edit)
                viewModel.getEditedEstateLiveData().observe(viewLifecycleOwner){ estate ->
                    this.id = estate.id
                    injectEstateToViews(estate)
                    initPictures(view)
                }
            }
        }

        val typesAdapter = EstateTypeSpinnerAdapter(resources)
        typeSpinner.adapter = typesAdapter
        typeSpinner.onItemSelectedListener = onSelectedType

        viewModel.getAgentListLiveData().value?.let { agentList ->
            val agentAdapter = EstateAgentSpinnerAdapter(agentList, resources)
            agentSpinner.adapter = agentAdapter
            agentSpinner.onItemSelectedListener = onSelectedAgent
        }

        entryDateButton.setOnClickListener(clickOnEntryDate)
        validationButton.setOnClickListener(clickOnValidButton)
    }

    private fun initPictures (view: View)  {
        view.findViewById<ImageView>(R.id.fragment_edit_estate_button_add_picture).setOnClickListener { launchPickPictureWithPickVisualMedia() }
        view.findViewById<ImageView>(R.id.fragment_edit_estate_button_take_picture).setOnClickListener { launchTakePictureWithCamera() }
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

    private val onSelectedAgent = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            agent = if (position == 0) { null }
            else {
                viewModel.getAgentListLiveData().value?.let {agentList -> agentList[position - 1] }
            }
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    private fun injectEstateToViews(estate: Estate) {
        entryDate = estate.entryDate
        entryDateButton.text = simpleDateFormat.format(Date(estate.entryDate))
        viewModel.getAgentListLiveData().value?.let { agentList ->
            val agent = agentList.find { agent -> agent.id == estate.agentId }
            val agentIndex = agentList.indexOf(agent)
            agentSpinner.setSelection(agentIndex)
        }
        typeSpinner.setSelection(estate.type.ordinal + 1)
        priceEditText.setText(estate.price.toString())
        descriptionEditText.setText(estate.description)
        surfaceEditText.setText(estate.surface.toString())
        numberOfRoomsEditText.setText(estate.numberOfRooms.toString())
        numberOfBathroomsEditText.setText(estate.numberOfBathrooms.toString())
        numberOfBedroomsEditText.setText(estate.numberOfBedrooms.toString())
        schoolDistanceEditText.setText(estate.schoolDistance.toString())
        shopDistanceEditText.setText(estate.shopDistance.toString())
        parkDistanceEditText.setText(estate.parkDistance.toString())
        houseNumberEditText.setText(estate.houseNumber.toString())
        streetEditText.setText(estate.street)
        additionalAddressEditText.setText(estate.additionalAddress)
        zipCodeEditText.setText(estate.zipCode)
        cityEditText.setText(estate.city)
        countryEditText.setText(estate.country)
        status = estate.status
    }

    private val clickOnEntryDate = OnClickListener {
        val entryDateCalendar = Calendar.getInstance()
        entryDate?.let { entryDateCalendar.timeInMillis = it }
        val datePickerDialog = DatePickerDialog(requireContext(), datePickerDialogListener, entryDateCalendar.get(Calendar.YEAR), entryDateCalendar.get(Calendar.MONTH), entryDateCalendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private val datePickerDialogListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val entryDateCalendar = Calendar.getInstance()
            entryDateCalendar.set(year, month, dayOfMonth)
            val entryDateMillis = entryDateCalendar.timeInMillis
            entryDate = entryDateMillis
            entryDateButton.text = simpleDateFormat.format(Date(entryDateMillis))
        }

    private val clickOnValidButton = OnClickListener {
        if (formIsCompleted()) {

            val houseNumber = houseNumberEditText.text.toString().toInt()
            val street = streetEditText.text.toString()
            val zipCode = zipCodeEditText.text.toString()
            val city = cityEditText.text.toString()
            val country = countryEditText.text.toString()
            val position = viewModel.geocodeAddress("$houseNumber $street, $zipCode $city, $country")

            val estate = Estate(
                id = this.id ?: return@OnClickListener,
                type = this.type ?: return@OnClickListener,
                price = priceEditText.text.toString().toDouble(),
                description = descriptionEditText.text.toString(),
                surface = surfaceEditText.text.toString().toFloat(),
                numberOfRooms = numberOfRoomsEditText.text.toString().toInt(),
                numberOfBathrooms = numberOfBathroomsEditText.text.toString().toInt(),
                numberOfBedrooms = numberOfBedroomsEditText.text.toString().toInt(),
                schoolDistance = schoolDistanceEditText.text.toString().toInt(),
                shopDistance = shopDistanceEditText.text.toString().toInt(),
                parkDistance = parkDistanceEditText.text.toString().toInt(),
                houseNumber = houseNumber,
                street = street,
                additionalAddress = additionalAddressEditText.text.toString(),
                zipCode = zipCode,
                city = city,
                country = country,
                latitude = position?.latitude,
                longitude = position?.longitude,
                status = status,
                entryDate = entryDate ?: return@OnClickListener,
                saleDate = null,
                agentId = agent?.id ?: return@OnClickListener
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
            this.entryDate == null -> Toast.makeText(activity, resources.getString(R.string.edit_estate_entry_date_required), Toast.LENGTH_SHORT).show()
            this.agent == null -> Toast.makeText(activity, resources.getString(R.string.edit_estate_agent_required), Toast.LENGTH_SHORT).show()
            this.type == null -> Toast.makeText(activity, resources.getString(R.string.edit_estate_type_required), Toast.LENGTH_SHORT).show()
            picturesAdapter.currentList.isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_picture_required), Toast.LENGTH_SHORT).show()
            priceEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_price_required), Toast.LENGTH_SHORT).show()
            descriptionEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_description_required), Toast.LENGTH_SHORT).show()
            surfaceEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_surface_required), Toast.LENGTH_SHORT).show()
            numberOfRoomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_numberOfRooms_required), Toast.LENGTH_SHORT).show()
            numberOfBathroomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_numberOfBathrooms_required), Toast.LENGTH_SHORT).show()
            numberOfBedroomsEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_numberOfBedrooms_required), Toast.LENGTH_SHORT).show()
            schoolDistanceEditText.text.toString().isEmpty() -> Toast.makeText(activity, "School distance is empty", Toast.LENGTH_SHORT).show()
            shopDistanceEditText.text.toString().isEmpty() -> Toast.makeText(activity, "Shop distance is empty", Toast.LENGTH_SHORT).show()
            parkDistanceEditText.text.toString().isEmpty() -> Toast.makeText(activity, "Park distance is empty", Toast.LENGTH_SHORT).show()
            houseNumberEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_houseNumber_required), Toast.LENGTH_SHORT).show()
            streetEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_street_required), Toast.LENGTH_SHORT).show()
            zipCodeEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_zipCode_required), Toast.LENGTH_SHORT).show()
            cityEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_city_required), Toast.LENGTH_SHORT).show()
            countryEditText.text.toString().isEmpty() -> Toast.makeText(activity, resources.getString(R.string.edit_estate_country_required), Toast.LENGTH_SHORT).show()
            else -> return true
        }
        return false
    }

    private fun addPictureToPictureList(pictureUri: Uri) {
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
                    val picture = Picture(System.currentTimeMillis(), id ?: return, pictureUri,editText.text.toString())
                    viewModel.addPicture(picture)
                    Toast.makeText(activity, resources.getString(R.string.picture_added), Toast.LENGTH_SHORT).show()
                }
            }
        })

        alertDialogBuilder.show()
    }

    private fun launchPickPictureWithPickVisualMedia() {
        registerForPickPictureResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun launchTakePictureWithCamera() {
        if (isCameraPermission()) {
            registerForTakePictureResult.launch(null)
        }
    }

    private val registerForPickPictureResult = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        if (uris.isNotEmpty()) {
            addPictureToPictureList(uris[0])
        }
    }

    private val registerForTakePictureResult = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val file = File(requireContext().externalCacheDir, CACHE_PICTURE_NAME)
            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        val outputStream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.flush()
                        outputStream.close()
                    }
                    addPictureToPictureList(file.toUri())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun isCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
            return false
        }
        return true
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            launchTakePictureWithCamera()
        }
    }

}