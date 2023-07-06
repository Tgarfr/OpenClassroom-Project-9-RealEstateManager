package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.Flow

interface EstateApi {

    fun getEstateListFlow(): Flow<List<Estate>>

    fun addEstate(estate: Estate)

    fun updateEstate(estate: Estate)

}