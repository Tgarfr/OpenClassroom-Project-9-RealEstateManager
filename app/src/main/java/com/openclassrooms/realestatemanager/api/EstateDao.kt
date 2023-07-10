package com.openclassrooms.realestatemanager.api

import androidx.room.*
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao: EstateApi {

    @Query("SELECT * FROM ${Estate.ROOM_TABLE_NAME}")
    override fun getEstateListFlow(): Flow<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override fun addEstate(estate: Estate): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addEstateList(estate: List<Estate>)

    @Update
    override fun updateEstate(estate: Estate): Int

}