package com.openclassrooms.realestatemanager.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate


class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var mapFragmentListener: MapFragmentListener
    private lateinit var context: Context
    private lateinit var viewModel: MapFragmentViewModel
    private lateinit var googleMap: GoogleMap
    private var agentMarker: Marker? = null

    interface MapFragmentListener {
        fun launchEstateSheetFragment(estate: Estate)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        context = requireContext()
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(context))[MapFragmentViewModel::class.java]

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map_fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        view.findViewById<ImageView>(R.id.fragment_map_button_center).setOnClickListener {
            getCurrentLocation()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MapFragmentListener) {
            mapFragmentListener = activity as MapFragmentListener
        } else {
            throw ClassCastException(activity.toString() + "must implement MapFragment.MapFragmentListener")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        getCurrentLocation()

        viewModel.getEstateListLiveData().observe(this) {   estateList ->
            estateList.forEach { estate ->
                val latitude = estate.latitude
                val longitude = estate.longitude
                if (latitude != null && longitude != null) {
                    googleMap.addMarker(MarkerOptions()
                        .position(LatLng(latitude, longitude))
                        .title(estate.id.toString()))
                }
            }
        }

        googleMap.setOnMarkerClickListener(object : OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                viewModel.getEstateFrom(marker.title?.toLong() ?: return true )?.let {
                    estate ->  mapFragmentListener.launchEstateSheetFragment(estate)
                }
                return true
            }

        })
    }

    private fun getCurrentLocation() {
        if (!checkLocationPermission()) return
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener{ location ->
                if (isAdded) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.5F))
                    val agentMarker = this.agentMarker
                    agentMarker?.let { agentMarker.remove() }
                    if (isAdded) {
                        this.agentMarker = googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_man))
                                .title(getString(R.string.map_position_agent))
                        )
                    }
                }
            }
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return false
        }
        return true
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            getCurrentLocation()
        }
    }

}