package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.PictureRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstateSheetFragmentViewModel(
    private val estateRepository: EstateRepository,
    private val pictureRepository: PictureRepository
    ): ViewModel() {

    private val estateListObserver: Observer<List<Estate>> = Observer { estateList ->
        if (estateRepository.getSelectedEstateLiveData().value == null && estateList.isNotEmpty()) {
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

    fun getPictureListLiveData(estateId: Long): LiveData<List<Picture>> =
        pictureRepository.getPictureListByEstateIdLiveData(estateId)

    fun getLocationString(estate: Estate?): String {
        if (estate != null) {
            if (estate.additionalAddress == null) {
                return "${estate.houseNumber} ${estate.street}\n${estate.city}\n${estate.zipCode}\n${estate.country}"
            }
            return "${estate.houseNumber} ${estate.street}\n${estate.additionalAddress}\n${estate.city}\n${estate.zipCode}\n${estate.country}"
        }
        return ""
    }

    fun validateEstateSale(estate: Estate, saleDate: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val soldEstate = Estate(
                id = estate.id,
                type = estate.type,
                price = estate.price,
                surface = estate.surface,
                numberOfRooms = estate.numberOfRooms,
                numberOfBathrooms = estate.numberOfBathrooms,
                numberOfBedrooms = estate.numberOfBedrooms,
                schoolDistance = estate.schoolDistance,
                shopDistance = estate.shopDistance,
                parkDistance = estate.parkDistance,
                description = estate.description,
                houseNumber = estate.houseNumber,
                street = estate.street,
                zipCode = estate.zipCode,
                city = estate.city,
                country = estate.country,
                additionalAddress = estate.additionalAddress,
                latitude = estate.latitude,
                longitude = estate.longitude,
                status = Estate.Status.SOLD,
                entryDate = estate.entryDate,
                saleDate = saleDate,
                agent = estate.agent
            )
            estateRepository.updateEstate(soldEstate)
            estateRepository.setSelectedEstateLiveData(soldEstate)
        }
    }

}