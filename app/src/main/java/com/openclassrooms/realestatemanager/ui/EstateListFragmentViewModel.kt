package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository

class EstateListFragmentViewModel(private val estateRepository: EstateRepository) : ViewModel() {

    fun getEstateListLiveData(): LiveData<List<Estate>> = estateRepository.getEstateListLiveData()

    fun getSelectedEstateLiveData(): LiveData<Estate> = estateRepository.getSelectedEstateLiveData()

    fun setSelectedEstateLiveData(estate: Estate) {
        estateRepository.setSelectedEstateLiveData(estate)
    }

}