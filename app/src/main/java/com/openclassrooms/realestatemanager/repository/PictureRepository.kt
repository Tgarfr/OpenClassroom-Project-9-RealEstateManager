package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.openclassrooms.realestatemanager.api.PictureDatabaseApi
import com.openclassrooms.realestatemanager.api.PictureStorageApi
import com.openclassrooms.realestatemanager.model.Picture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PictureRepository (
    private val pictureDatabaseApi: PictureDatabaseApi,
    private val pictureStorageApi: PictureStorageApi
    ) {

    private val pictureListLiveData = pictureDatabaseApi.getPictureListFlow().asLiveData()

    fun getPictureListLiveData() = pictureListLiveData

    fun getPictureListByEstateIdLiveData(estateId: Long): LiveData<List<Picture>> =
        pictureListLiveData.map {
                initialPictureList -> initialPictureList.let {
                    notNullPictureList -> notNullPictureList.filter {
                        picture -> picture.estateId == estateId }
        }
    }

    fun addPictureList(pictureList: List<Picture>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (picture in pictureList) {
                addPicture(Picture(picture.id, picture.estateId, picture.getUri(), picture.description))
            }
        }
    }

    fun deletePicture(picture: Picture) {
        CoroutineScope(Dispatchers.IO).launch {
            pictureStorageApi.deletePicture(picture)
            pictureDatabaseApi.deletePicture(picture)
        }
    }

    private fun addPicture(picture: Picture) {
        val storedPicture = pictureStorageApi.addPicture(picture)
        storedPicture?.let { pictureDatabaseApi.addPicture(storedPicture) }
    }

}