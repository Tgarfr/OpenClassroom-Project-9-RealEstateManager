package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.api.FakeEstateApi
import com.openclassrooms.realestatemanager.repository.EstateRepository


object Injection {

    val estateRepository by lazy { EstateRepository(FakeEstateApi) }

}