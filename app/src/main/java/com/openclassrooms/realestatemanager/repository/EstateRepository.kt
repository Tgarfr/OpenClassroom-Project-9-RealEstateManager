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

    fun editEstate(editedEstate: Estate) {
        val estateList: List<Estate> = estateListLiveData.value ?: return
        val mutableEstateListWithEditedEstate: MutableList<Estate> = mutableListOf()
        for (estate in estateList) {
            if (estate.id == editedEstate.id) {
                mutableEstateListWithEditedEstate.add(editedEstate)
            } else {
                mutableEstateListWithEditedEstate.add(estate)
            }
        }
        estateListLiveData.value = mutableEstateListWithEditedEstate.toList()
        selectedEstateLiveData.value = editedEstate
    }

    fun getSelectedEstateLiveData(): LiveData<Estate> = selectedEstateLiveData

    fun setSelectedEstateLiveData(estate: Estate) { selectedEstateLiveData.value = estate }

}