package com.openclassrooms.realestatemanager.api

import androidx.room.*
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao: EstateApi {

    companion object {
        private const val ESTATE_TABLE = "estate"
    }

    @Query("SELECT * FROM $ESTATE_TABLE")
    override fun getEstateListFlow(): Flow<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override fun addEstate(estate: Estate)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addEstateList(estate: List<Estate>)

    @Update
    override fun updateEstate(estate: Estate)

}