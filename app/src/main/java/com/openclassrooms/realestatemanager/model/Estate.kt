package com.openclassrooms.realestatemanager.model

import java.util.*

data class Estate(
    val id: Int,
    val name: String,
    val type: Type,
    val price: Double,
    val surface: Float,
    val numberOfRooms: Int,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val description: String,
    val houseNumber: Int,
    val street: String,
    val zipCode: String,
    val city: String,
    val country: String,
    val additionalAddress: String?,
    val status: Int,
    val entryDate: Calendar,
    val saleDate: Calendar?,
    val agent: String
) {

    enum class Type {
        HOUSE, DUPLEX, FLAT, PENTHOUSE
    }
}