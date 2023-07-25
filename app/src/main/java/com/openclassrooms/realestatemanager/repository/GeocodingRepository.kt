package com.openclassrooms.realestatemanager.repository

import android.location.Location
import com.openclassrooms.realestatemanager.api.GeocodingApi

class GeocodingRepository(private val geocodingApi: GeocodingApi) {

    fun geocodeAddress(addressString: String): Location? = geocodingApi.geocodeAddress(addressString)

    fun isInSector(sectorString: String, latitude: Double, longitude: Double): Boolean =
        geocodingApi.isInSector(sectorString, latitude, longitude)

}