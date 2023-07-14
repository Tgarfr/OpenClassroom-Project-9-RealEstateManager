package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository

class MapFragmentViewModel(
    private val estateRepository: EstateRepository
    ): ViewModel() {

    fun getEstateListLiveData(): LiveData<List<Estate>> = estateRepository.getEstateListLiveData()


    fun getEstateFrom(estateId: Long): Estate? =
        estateRepository.getEstateListLiveData().value?.single { estate -> estate.id == estateId }

}