package com.openclassrooms.realestatemanager.api

import android.location.Location

interface GeocodingApi {

    fun geocodeAddress(addressString: String): Location?

    fun isInSector(sectorString: String, latitude: Double, longitude: Double): Boolean

}