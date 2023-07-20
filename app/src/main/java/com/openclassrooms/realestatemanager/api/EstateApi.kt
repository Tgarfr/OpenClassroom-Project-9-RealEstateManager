package com.openclassrooms.realestatemanager.api

import android.database.Cursor
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.flow.Flow

interface EstateApi {

    fun getEstateListFlow(): Flow<List<Estate>>

    fun getEstateListCursor(): Cursor

    fun addEstate(estate: Estate): Long

    fun updateEstate(estate: Estate): Int

}