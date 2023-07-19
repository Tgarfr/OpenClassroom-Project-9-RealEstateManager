package com.openclassrooms.realestatemanager.ui

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.GeocodingRepository
import com.openclassrooms.realestatemanager.repository.PictureRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstateEditFragmentViewModel(
    private val estateRepository: EstateRepository,
    private val pictureRepository: PictureRepository,
    private val geocodingRepository: GeocodingRepository
) : ViewModel() {

    private enum class Setting { ADD, EDIT }
    private lateinit var setting: Setting
    private val newPictureListLiveData = MutableLiveData(listOf<Picture>())


    fun getEditedEstateLiveData(): LiveData<Estate> {
        setting = Setting.EDIT
        return estateRepository.getSelectedEstateLiveData()
    }

    fun getPictureListLiveData(estateId: Long): LiveData<List<Picture>> {
        return when(setting) {
            Setting.ADD -> newPictureListLiveData
            Setting.EDIT -> {
                val mergedPictureListLiveData = MediatorLiveData<List<Picture>>(listOf())
                mergedPictureListLiveData.addSource(pictureRepository.getPictureListByEstateIdLiveData(estateId)) { pictureList ->
                    mergedPictureListLiveData.value = mergedPictureListLiveData.value?.plus(pictureList)?.distinctBy { picture -> picture.id }
                }
                mergedPictureListLiveData.addSource(newPictureListLiveData) { pictureList ->
                    mergedPictureListLiveData.value = mergedPictureListLiveData.value?.plus(pictureList)?.distinctBy { picture -> picture.id }
                }
                mergedPictureListLiveData
            }
        }
    }

    fun setNewEditedEstate() {
        setting = Setting.ADD
    }

    fun addEstate(estate: Estate) {
        CoroutineScope(Dispatchers.IO).launch {
            val estateId = estateRepository.addEstate(estate)
            if (estateId == estate.id) {
                pictureRepository.addPictureList(newPictureListLiveData.value ?: return@launch)
            }
        }
    }

    fun updateEstate(estate: Estate) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = estateRepository.updateEstate(estate)
            if (result > 0) {
                pictureRepository.addPictureList(newPictureListLiveData.value ?: return@launch)
            }
        }
    }

    fun addPicture(picture :Picture) {
        val pictureList = newPictureListLiveData.value?.toMutableList() ?: return
        pictureList.add(picture)
        newPictureListLiveData.value = pictureList.toList()
    }

    fun geocodeAddress(addressString: String): Location? = geocodingRepository.geocodeAddress(addressString)

}