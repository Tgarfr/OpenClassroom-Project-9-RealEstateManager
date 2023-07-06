package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.api.EstateApi
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstateRepository(private val estateApi: EstateApi) {

    private val estateListLiveData = estateApi.getEstateListFlow().asLiveData()
    private val selectedEstateLiveData: MutableLiveData<Estate> = MutableLiveData<Estate>()

    fun getEstateListLiveData(): LiveData<List<Estate>> = estateListLiveData

    fun addEstate(estate: Estate) {
        CoroutineScope(Dispatchers.IO).launch {
            estateApi.addEstate(estate)
        }
    }

    fun updateEstate(estate: Estate) {
        CoroutineScope(Dispatchers.IO).launch {
            estateApi.updateEstate(estate)
        }
    }

    fun getSelectedEstateLiveData(): LiveData<Estate> = selectedEstateLiveData

    fun setSelectedEstateLiveData(estate: Estate) { selectedEstateLiveData.value = estate }

}