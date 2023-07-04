package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.api.EstateApi
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstateRepository private constructor(private val estateApi: EstateApi) {

    private val estateListLiveData = MutableLiveData<List<Estate>>(null)
    private val selectedEstateLiveData: MutableLiveData<Estate> = MutableLiveData<Estate>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            estateListLiveData.postValue(estateApi.getEstateList())
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: EstateRepository? = null

        fun getInstance(estateApi: EstateApi): EstateRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: EstateRepository(estateApi).also { INSTANCE = it }
            }
    }


    fun getEstateListLiveData(): LiveData<List<Estate>> = estateListLiveData

    fun addEstate(estate: Estate) {
        val mutableEstateList = estateListLiveData.value?.toMutableList() ?: return
        mutableEstateList.add(estate)
        estateListLiveData.value = mutableEstateList.toList()
        CoroutineScope(Dispatchers.IO).launch {
            estateApi.addEstate(estate)
        }
    }

    fun updateEstate(estate: Estate) {
        estateListLiveData.value = estateListLiveData.value?.map {
            if (it.id == estate.id) estate else it
        } ?: return
        CoroutineScope(Dispatchers.IO).launch {
            estateApi.updateEstate(estate)
        }
    }

    fun getSelectedEstateLiveData(): LiveData<Estate> = selectedEstateLiveData

    fun setSelectedEstateLiveData(estate: Estate) { selectedEstateLiveData.value = estate }

}