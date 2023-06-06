package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Estate

interface EstateApi {

    fun getEstateList(): List<Estate>
}