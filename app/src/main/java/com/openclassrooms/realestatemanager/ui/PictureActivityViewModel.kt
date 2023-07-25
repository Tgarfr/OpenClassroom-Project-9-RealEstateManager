package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.repository.PictureRepository

class PictureActivityViewModel(
    private val pictureRepository: PictureRepository
    ): ViewModel() {

    fun getPictureListByEstateIdLiveData(estateId: Long): LiveData<List<Picture>> =
        pictureRepository.getPictureListByEstateIdLiveData(estateId)

    fun getPictureById(pictureId: Long): Picture? =
        pictureRepository.getPictureListLiveData().value?.find { picture ->  picture.id == pictureId }

    fun deletePicture(picture: Picture) {
        pictureRepository.deletePicture(picture)
    }

}