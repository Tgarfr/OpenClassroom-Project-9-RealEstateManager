package com.openclassrooms.realestatemanager.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.Picture
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao: PictureDatabaseApi {

    @Query("SELECT * FROM ${Picture.ROOM_TABLE_NAME}")
    override fun getPictureListFlow(): Flow<List<Picture>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override fun addPicture(picture: Picture)

    @Delete
    override fun deletePicture(picture: Picture)

}