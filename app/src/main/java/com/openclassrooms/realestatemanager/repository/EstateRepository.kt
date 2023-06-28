package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.api.EstateApi
import com.openclassrooms.realestatemanager.model.Estate

class EstateRepository(estateApi: EstateApi) {

    private val estateListLiveData: MutableLiveData<List<Estate>> = MutableLiveData(estateApi.getEstateList())
    private val selectedEstateLiveData: MutableLiveData<Estate> = MutableLiveData<Estate>()

    fun getEstateListLiveData(): LiveData<List<Estate>> = estateListLiveData

    fun addEstate(estate: Estate) {
        val mutableEstateList: MutableList<Estate> = estateListLiveData.value?.toMutableList() ?: return
        mutableEstateList.add(estate)
        estateListLiveData.value = mutableEstateList.toList()
    }

    fun getSelectedEstateLiveData(): LiveData<Estate> = selectedEstateLiveData

    fun setSelectedEstateLiveData(estate: Estate) { selectedEstateLiveData.value = estate }

}