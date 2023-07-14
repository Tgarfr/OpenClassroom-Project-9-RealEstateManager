package com.openclassrooms.realestatemanager.model

import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import android.os.Build
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.R

@Entity(tableName = Estate.ROOM_TABLE_NAME)
data class Estate(
    @PrimaryKey val id: Long,
    val type: Type,
    val price: Double,
    val surface: Float,
    val numberOfRooms: Int,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val description: String,
    val houseNumber: Int,
    val street: String,
    val additionalAddress: String?,
    val zipCode: String,
    val city: String,
    val country: String,
    var latitude: Double?,
    var longitude: Double?,
    val status: Status,
    val entryDate: Long,
    val saleDate: Long?,
    val agent: String
) {

    companion object {
        const val ROOM_TABLE_NAME : String = "estate"
    }

    fun geocodePosition(context: Context) {
        val geocoder = Geocoder(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName("$houseNumber $street, $zipCode $city, $country, ",1) { addresses ->
                latitude = addresses[0].latitude
                longitude = addresses[0].longitude
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName("$houseNumber $street, $zipCode $city, $country, ",1)
             addresses?.let {
                 latitude = it[0].latitude
                 longitude = it[0].longitude
             }
        }
    }

    enum class Type(private val type: Int) {

        HOUSE(1),
        DUPLEX(2),
        FLAT(3),
        PENTHOUSE(4);

        fun getString(resources: Resources): String? {
            return when (type) {
                FLAT.type -> resources.getString(R.string.estate_type_flat)
                HOUSE.type -> resources.getString(R.string.estate_type_house)
                DUPLEX.type -> resources.getString(R.string.estate_type_duplex)
                PENTHOUSE.type -> resources.getString(R.string.estate_type_penthouse)
                else -> null
            }
        }

    }

    enum class Status(private val status: Int) {

        AVAILABLE(0),
        SOLD(1);

        fun getString(resources: Resources): String? {
            return when (status) {
                AVAILABLE.status -> resources.getString(R.string.estate_status_available)
                SOLD.status -> resources.getString(R.string.estate_status_sold)
                else -> null
            }
        }

    }

}