package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository

class EstateEditFragmentViewModel(private val estateRepository: EstateRepository) : ViewModel() {

    fun getSelectedEstateLiveData(): LiveData<Estate> = estateRepository.getSelectedEstateLiveData()

    fun editEstate(estate: Estate) {
        estateRepository.editEstate(estate)
    }

}