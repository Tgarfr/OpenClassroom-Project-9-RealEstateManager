package com.openclassrooms.realestatemanager.api

import androidx.room.*
import com.openclassrooms.realestatemanager.model.Estate

@Dao
interface EstateDao: EstateApi {

    companion object {
        private const val ESTATE_TABLE = "estate"
    }

    @Query("SELECT * FROM $ESTATE_TABLE")
    override suspend fun getEstateList(): List<Estate>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun addEstate(estate: Estate)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addEstateList(estate: List<Estate>)

    @Update
    override suspend fun updateEstate(estate: Estate)

}