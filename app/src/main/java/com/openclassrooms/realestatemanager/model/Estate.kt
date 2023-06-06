package com.openclassrooms.realestatemanager.model

import java.util.*

data class Estate(
    val id: Int,
    val name: String,
    val type: Type,
    val price: Double,
    val surface: Float,
    val numberOfRoom: Int,
    val description: String,
    val housseNumber: Int,
    val street: String,
    val ZIPCode: String,
    val city: String,
    val status: Int,
    val entryDate: Calendar,
    var saleDate: Calendar?,
    val agent: String
) {

    enum class Type {
        HOUSE, DUPLEX, FLAT, PENTHOUSE
    }
}