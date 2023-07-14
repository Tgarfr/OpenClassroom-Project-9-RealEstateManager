package com.openclassrooms.realestatemanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
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
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture

class EstateSheetFragment(
    private val estateSheetFragmentListener: EstateSheetFragmentListener
    ) : Fragment(), EstatePicturesAdapter.EstatePicturesAdapterListener, OnMapReadyCallback {

    interface EstateSheetFragmentListener {
        fun launchEstateEditFragment(estate: Estate)
    }

    private lateinit var viewModel: EstateSheetFragmentViewModel
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_sheet_estate, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[EstateSheetFragmentViewModel::class.java]

        val statusTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_status)
        val descriptionTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_description)
        val surfaceTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_surface)
        val numbersOfRoomsTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_rooms)
        val numbersOfBathroomsTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_bathrooms)
        val numbersOfBedroomsTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_numbers_of_bedrooms)
        val schoolDistanceEditText: TextView = view.findViewById(R.id.fragment_sheet_estate_distance_school)
        val shopDistanceEditText: TextView = view.findViewById(R.id.fragment_sheet_estate_distance_shop)
        val parkDistanceEditText: TextView = view.findViewById(R.id.fragment_sheet_estate_distance_park)
        val locationTextView: TextView = view.findViewById(R.id.fragment_sheet_estate_location)
        view.findViewById<ImageView>(R.id.fragment_sheet_estate_edit_button).setOnClickListener {
            estateSheetFragmentListener.launchEstateEditFragment(viewModel.getSelectedEstateLiveData().value ?: return@setOnClickListener )
        }

        val adapter = EstatePicturesAdapter(requireContext(), this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_sheet_estate_pictures_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        viewModel.getSelectedEstateLiveData().observe(viewLifecycleOwner) { estate ->
            if (estate != null) {
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
            }
        }

        val mapView = view.findViewById<MapView>(R.id.fragment_sheet_estate_map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
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

}