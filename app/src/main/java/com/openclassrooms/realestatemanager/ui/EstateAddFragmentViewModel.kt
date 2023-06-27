package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository

class EstateAddFragmentViewModel(private val estateRepository: EstateRepository) : ViewModel() {

    fun addEstate(estate: Estate) {
        estateRepository.addEstate(estate)
    }

}