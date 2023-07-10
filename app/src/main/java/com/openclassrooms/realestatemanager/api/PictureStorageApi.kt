package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Picture

interface PictureStorageApi {

    fun addPicture(picture: Picture): Picture?

    fun deletePicture(picture: Picture)

}