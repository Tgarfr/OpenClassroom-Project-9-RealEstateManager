package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository

class EstateSheetFragmentViewModel(private val estateRepository: EstateRepository): ViewModel() {

    private val estateListObserver: Observer<List<Estate>> = Observer { estateList ->
        if (estateRepository.getSelectedEstateLiveData().value == null && !estateList.isNullOrEmpty()) {
            estateRepository.setSelectedEstateLiveData(estateList[0])
        }
    }

    init {
        estateRepository.getEstateListLiveData().observeForever(estateListObserver)
    }

    override fun onCleared() {
        estateRepository.getEstateListLiveData().removeObserver(estateListObserver)
        super.onCleared()
    }

    fun getSelectedEstateLiveData(): LiveData<Estate> = estateRepository.getSelectedEstateLiveData()

    fun getLocationString(estate: Estate?): String {
        if (estate != null) {
            if (estate.additionalAddress == null) {
                return "{$estate.houseNumber} ${estate.street}\n${estate.city}\n${estate.zipCode}\n${estate.country}"
            }
            return "${estate.houseNumber} ${estate.street}\n${estate.additionalAddress}\n${estate.city}\n${estate.zipCode}\n${estate.country}"
        }
        return ""
    }

}