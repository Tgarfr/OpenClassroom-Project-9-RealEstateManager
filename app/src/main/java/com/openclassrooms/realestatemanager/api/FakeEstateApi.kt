package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("unused")
class FakeEstateApi : EstateApi {

    private var mutableEstateList = estateList.toMutableList()
    private var estateListFlow = MutableStateFlow(estateList.toList())

    override fun getEstateListFlow() = estateListFlow

    override fun addEstate(estate: Estate): Long {
        mutableEstateList.add(estate)
        estateListFlow.value = mutableEstateList.toList()
        return estate.id
    }

    override fun updateEstate(estate: Estate): Int {
        mutableEstateList = mutableEstateList.map {
            if (it.id == estate.id) estate else it
        }.toMutableList()
        estateListFlow.value = mutableEstateList.toList()
        return 1
    }

    companion object {
        private const val DESCRIPTION =
            "Anchored bay a vast marble gallery with sweeping staircase, the entertaining floor includes a baronial living room facing Park Avenue, handsome library with original paneling, and tremendous dining room; all of which enjoy fireplaces. The state-of-the-art St. Charles designed kitchen includes a sunny breakfast room and staff quarters. Upstairs, the expansive master suite overlooks Park Avenue and includes two marble baths, two dressing rooms, and two offices. Additionally the are three large bedrooms with en-suite bath and media."

        fun getEstateList() = estateList

        private val estateList = listOf(
            Estate(
                id = 1,
                type = Estate.Type.HOUSE,
                price = 250000.00,
                surface = 100F,
                numberOfRooms = 3,
                numberOfBathrooms = 2,
                numberOfBedrooms = 5,
                schoolDistance = 15,
                shopDistance = 200,
                parkDistance = 50,
                description = DESCRIPTION,
                houseNumber = 1,
                street = "Oak street",
                zipCode = "10001",
                city = "New York",
                country = "United States",
                additionalAddress = "Apt 6/7A",
                latitude = 47.79932966201001,
                longitude = 2.144782738248518,
                status = Estate.Status.AVAILABLE,
                entryDate = 1672531200000,
                saleDate = null,
                agentId = 0L
            ),
            Estate(
                id = 3,
                type = Estate.Type.DUPLEX,
                price = 1000000.00,
                surface = 100F,
                numberOfRooms = 3,
                numberOfBathrooms = 2,
                numberOfBedrooms = 5,
                schoolDistance = 15,
                shopDistance = 200,
                parkDistance = 50,
                description = DESCRIPTION,
                houseNumber = 1,
                street = "Oak street",
                zipCode = "10001",
                city = "Montauk",
                country = "United States",
                additionalAddress = "Apt 6/7A",
                latitude = 47.796406482918876,
                longitude = 2.125695038996438,
                status = Estate.Status.AVAILABLE,
                entryDate = 1675209600000,
                saleDate = null,
                agentId = 2L
            ),
            Estate(
                id = 4,
                type = Estate.Type.PENTHOUSE,
                price = 75000.50,
                surface = 100F,
                numberOfRooms = 3,
                numberOfBathrooms = 2,
                numberOfBedrooms = 5,
                schoolDistance = 15,
                shopDistance = 200,
                parkDistance = 50,
                description = DESCRIPTION,
                houseNumber = 1,
                street = "Oak street",
                zipCode = "10001",
                city = "Hampton Bays",
                country = "United States",
                additionalAddress = "Apt 6/7A",
                latitude = 47.80414590516796,
                longitude = 2.131085088785238,
                status = Estate.Status.SOLD,
                entryDate = 1677628800000,
                saleDate = null,
                agentId = 3L
            ),
            Estate(
                id = 5,
                type = Estate.Type.FLAT,
                price = 150000.30,
                surface = 100F,
                numberOfRooms = 3,
                numberOfBathrooms = 2,
                numberOfBedrooms = 5,
                schoolDistance = 15,
                shopDistance = 200,
                parkDistance = 50,
                description = DESCRIPTION,
                houseNumber = 1,
                street = "Oak street",
                zipCode = "10001",
                city = "New York",
                country = "United States",
                additionalAddress = "Apt 6/7A",
                latitude = 47.80341519356079,
                longitude = 2.1364256885759754,
                status = Estate.Status.AVAILABLE,
                entryDate = 1680307200000,
                saleDate = null,
                agentId = 2L
            )
        )
    }

}