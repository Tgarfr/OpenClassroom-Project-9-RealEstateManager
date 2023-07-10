package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Picture
import kotlinx.coroutines.flow.Flow

interface  PictureDatabaseApi {

    fun getPictureListFlow(): Flow<List<Picture>>

    fun addPicture(picture: Picture)

    fun deletePicture(picture: Picture)

}