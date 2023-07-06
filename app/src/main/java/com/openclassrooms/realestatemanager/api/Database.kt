package com.openclassrooms.realestatemanager.api

import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.model.Estate

@androidx.room.Database(entities = [Estate::class], version = 1)
abstract class Database: RoomDatabase() {

    abstract fun estateDao(): EstateDao

    companion object {
        const val DATABASE_NAME: String = "RealEstateManagerDatabase"
    }

}