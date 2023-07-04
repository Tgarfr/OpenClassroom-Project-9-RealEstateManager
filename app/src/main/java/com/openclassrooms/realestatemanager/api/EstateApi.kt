package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Estate

interface EstateApi {

    suspend fun getEstateList(): List<Estate>

    suspend fun addEstate(estate: Estate)

    suspend fun updateEstate(estate: Estate)

}