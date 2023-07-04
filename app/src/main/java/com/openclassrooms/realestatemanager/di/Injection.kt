package com.openclassrooms.realestatemanager.di

import android.content.Context
import com.openclassrooms.realestatemanager.api.Database
import com.openclassrooms.realestatemanager.repository.EstateRepository


class Injection private constructor(context: Context) {

    private val database by lazy { Database.getInstance(context) }
    private val estateApi by lazy { database.estateDao() }

    companion object {
        @Volatile
        private var INSTANCE: Injection? = null

        fun getInstance(context: Context): Injection =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Injection(context).also { INSTANCE = it }
            }
    }

    val estateRepository by lazy { EstateRepository.getInstance(estateApi) }

}