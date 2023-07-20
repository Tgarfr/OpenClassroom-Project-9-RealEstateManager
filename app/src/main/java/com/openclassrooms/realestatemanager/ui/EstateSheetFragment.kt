package com.openclassrooms.realestatemanager.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EstateSheetFragment(
    private val estateSheetFragmentListener: EstateSheetFragmentListener
    ) : Fragment(), EstatePicturesAdapter.EstatePicturesAdapterListener, OnMapReadyCallback {

    interface EstateSheetFragmentListener {
        fun launchEstateEditFragment(estate: Estate)
    }

    private lateinit var viewModel: EstateSheetFragmentViewModel
    private var estate: Estate? = null
    private var agent: Agent? = null
    private lateinit var adapter: EstatePicturesAdapter
    private lateinit var googleMap: GoogleMap
    private val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    private lateinit var entryDateTextView: TextView
    private lateinit var agentTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var surfaceTextView: TextView
    private lateinit var numbersOfRoomsTextView: TextView
    private lateinit var numbersOfBathroomsTextView: TextView
    private lateinit var numbersOfBedroomsTextView: TextView
    private lateinit var schoolDistanceEditText: TextView
    private lateinit var shopDistanceEditText: TextView
    private lateinit var parkDistanceEditText: TextView
    private lateinit var locationTextView: TextView
    private lateinit var validateSaleImageView: ImageView
    private lateinit var saleDateTitleTextView: TextView
    private lateinit var saleDateTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_sheet_estate, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[EstateSheetFragmentViewModel::class.java]
        initViews(view)

        viewModel.getSelectedEstateLiveData().observe(viewLifecycleOwner) { estate -> setEstate(estate)}

        view.findViewById<ImageView>(R.id.fragment_sheet_estate_edit_button).setOnClickListener {
            estateSheetFragmentListener.launchEstateEditFragment(viewModel.getSelectedEstateLiveData().value ?: return@setOnClickListener )
        }

        adapter = EstatePicturesAdapter(requireContext(), this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_sheet_estate_pictures_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val mapView = view.findViewById<MapView>(R.id.fragment_sheet_estate_map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    private fun initViews(view: View) {
        entryDateTextView = view.findViewById(R.id.fragment_sheet_estate_entry_date)
        agentTextView = view.findViewById(R.id.fragment_sheet_estate_agent)
        statusTextView = view.findViewById(R.id.fragment_sheet_estate_status)
        descriptionTextView = view.findViewById(R.id.fragment_sheet_estate_description)
        surfaceTextView = view.findViewById(R.id.fragment_sheet_estate_surface)
        numbersOfRoomsTextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_rooms)
        numbersOfBathroomsTextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_bathrooms)
        numbersOfBedroomsTextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_bedrooms)
        schoolDistanceEditText = view.findViewById(R.id.fragment_sheet_estate_distance_school)
        shopDistanceEditText = view.findViewById(R.id.fragment_sheet_estate_distance_shop)
        parkDistanceEditText = view.findViewById(R.id.fragment_sheet_estate_distance_park)
        locationTextView = view.findViewById(R.id.fragment_sheet_estate_location)
        validateSaleImageView = view.findViewById(R.id.fragment_sheet_estate_sale_validate)
        saleDateTitleTextView = view.findViewById(R.id.fragment_sheet_estate_title_sale_date)
        saleDateTextView = view.findViewById(R.id.fragment_sheet_estate_sale_date)
    }

    private fun setEstate(estate: Estate?) {
        this.estate = estate
        if (estate != null) {
            entryDateTextView.text = simpleDateFormat.format(Date(estate.entryDate))
            agent = viewModel.getAgentListLiveData().value?.let { agentList -> agentList.find { agent -> agent.id == estate.agentId } }
            agentTextView.text = agent?.name
            statusTextView.text = estate.status.getString(resources)
            descriptionTextView.text = estate.description
            surfaceTextView.text = estate.surface.toString()
            numbersOfRoomsTextView.text = estate.numberOfRooms.toString()
            numbersOfBathroomsTextView.text = estate.numberOfBathrooms.toString()
            numbersOfBedroomsTextView.text = estate.numberOfBedrooms.toString()
            schoolDistanceEditText.text = estate.schoolDistance.toString()
            shopDistanceEditText.text = estate.shopDistance.toString()
            parkDistanceEditText.text = estate.parkDistance.toString()
            locationTextView.text = viewModel.getLocationString(estate)
            viewModel.getPictureListLiveData(estate.id).observe(viewLifecycleOwner) { pictureList -> adapter.submitList(pictureList) }
            if (::googleMap.isInitialized) {
                onMapReady(this.googleMap)
            }
            when(estate.status) {
                Estate.Status.AVAILABLE -> {
                    validateSaleImageView.isGone = false
                    validateSaleImageView.setOnClickListener(onClickSaleValidateIcon)
                    saleDateTitleTextView.isGone = true
                    saleDateTextView.isGone = true

                }
                Estate.Status.SOLD -> {
                    validateSaleImageView.isGone = true
                    saleDateTitleTextView.isGone = false
                    saleDateTextView.isGone = false
                    estate.saleDate?.let {saleDateTextView.text = simpleDateFormat.format(Date(it))}
                }
            }
        }
    }

    override fun clickOnPicture(picture: Picture) {
        val intent = Intent(requireContext(), PictureActivity::class.java)
        intent.putExtra(PictureActivity.BUNDLE_PICTURE_ID_KEY, picture.id)
        ActivityCompat.startActivity(requireContext(), intent, null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        viewModel.getSelectedEstateLiveData().value?.let { estate ->
            val latitude = estate.latitude
            val longitude = estate.longitude
            if (latitude != null && longitude != null) {
                val latLng = LatLng(latitude, longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13F))
                googleMap.clear()
                googleMap.addMarker(MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.map_position_estate)))
            }
        }
    }

    private val onClickSaleValidateIcon = OnClickListener {
        val validationDialog = AlertDialog.Builder(requireContext())
        validationDialog.setTitle(getString(R.string.sheet_estate_sale_validation))
            .setMessage(getString(R.string.sheet_estate_sale_validation_ask))
            .setPositiveButton(getString(R.string.sheet_estate_sale_yes)) { _, _ -> pickSaleDate() }
            .setNegativeButton(getString(R.string.sheet_estate_sale_no)) { _, _ -> }
            .setCancelable(false)
            .show()
    }

    private fun pickSaleDate() {
        val actualDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(requireContext(), datePickerDialogListener, actualDate.get(
            Calendar.YEAR), actualDate.get(Calendar.MONTH), actualDate.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private val datePickerDialogListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val saleDate = Calendar.getInstance()
            saleDate.set(year, month, dayOfMonth)
            estate?.let { viewModel.validateEstateSale(it, saleDate.timeInMillis) }
        }

}