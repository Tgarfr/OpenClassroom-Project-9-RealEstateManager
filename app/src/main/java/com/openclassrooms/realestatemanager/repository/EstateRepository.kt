package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.api.EstateApi
import com.openclassrooms.realestatemanager.model.Estate

class EstateRepository(private val estateApi: EstateApi) {

    private val estateListLiveData = estateApi.getEstateListFlow().asLiveData()
    private val selectedEstateLiveData: MutableLiveData<Estate> = MutableLiveData<Estate>()

    fun getEstateListLiveData(): LiveData<List<Estate>> = estateListLiveData

    fun getEstateListCursor() = estateApi.getEstateListCursor()

    fun addEstate(estate: Estate): Long {
        return estateApi.addEstate(estate)
    }

    fun updateEstate(estate: Estate): Int {
        return estateApi.updateEstate(estate)
    }

    fun getSelectedEstateLiveData(): LiveData<Estate> = selectedEstateLiveData

    fun setSelectedEstate(estate: Estate) { selectedEstateLiveData.postValue(estate) }

}