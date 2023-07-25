package com.openclassrooms.realestatemanager.api

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build

class GeocodingAndroidApi(private val context: Context): GeocodingApi {

    companion object {
        private const val LOCATION_TITLE = "Location"
        private const val SECTOR_FILTER_DISTANCE_IN_METER = 1000
    }

    override fun geocodeAddress(addressString: String): Location? {
        var location: Location? = null
        val geocoder = Geocoder(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(addressString,1) { addresses ->
                location = getLocationFromAddresses(addresses)
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(addressString,1)
            addresses?.let {
                location = getLocationFromAddresses(addresses)
            }
        }
        return location
    }

    override fun isInSector(sectorString: String, latitude: Double, longitude: Double): Boolean {
        val estateLocation = Location(LOCATION_TITLE)
        estateLocation.latitude = latitude
        estateLocation.longitude = longitude
        val sectorLocation = geocodeAddress(sectorString)
        return if (sectorLocation != null) {
            estateLocation.distanceTo(sectorLocation) <= SECTOR_FILTER_DISTANCE_IN_METER
        } else false
    }

    private fun getLocationFromAddresses(addresses: List<Address>): Location? {
        if (addresses.isNotEmpty()) {
            val location = Location(LOCATION_TITLE)
            location.latitude = addresses[0].latitude
            location.longitude = addresses[0].longitude
            return location
        }
        return null
    }

}