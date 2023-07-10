package com.openclassrooms.realestatemanager.api

import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture

@androidx.room.Database(entities = [Estate::class, Picture::class], version = 1)
abstract class Database: RoomDatabase() {

    abstract fun estateDao(): EstateDao
    abstract fun pictureDao(): PictureDao

    companion object {
        const val DATABASE_NAME: String = "RealEstateManagerDatabase"
    }

}