package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.api.EstateApi
import com.openclassrooms.realestatemanager.model.Estate

class EstateRepository(private val estateApi: EstateApi) {

    fun getEstateList(): List<Estate> = estateApi.getEstateList()

}