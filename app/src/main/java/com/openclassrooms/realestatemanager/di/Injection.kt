package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.api.Database
import com.openclassrooms.realestatemanager.api.FakeEstateApi
import com.openclassrooms.realestatemanager.api.PictureStorage
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.GeocodingRepository
import com.openclassrooms.realestatemanager.repository.PictureRepository
import com.openclassrooms.realestatemanager.repository.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Injection private constructor(context: Context) {

    private val database: Database by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            Database.DATABASE_NAME
        )
            .addCallback(prepopulateDatabase)
            .build()
    }

    private val estateApi by lazy { database.estateDao() }
    private val pictureDatabaseApi by lazy { database.pictureDao() }
    private val pictureStorageApi by lazy { PictureStorage(context) }

    val estateRepository by lazy { EstateRepository(estateApi) }
    val pictureRepository by lazy { PictureRepository(pictureDatabaseApi, pictureStorageApi) }
    val searchRepository by lazy { SearchRepository() }
    val geocodingRepository by lazy { GeocodingRepository(context) }
    val agentRepository by lazy { AgentRepository() }

    companion object {
        @Volatile
        private var INSTANCE: Injection? = null

        fun getInstance(context: Context): Injection =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Injection(context).also { INSTANCE = it }
            }
    }

    private val prepopulateDatabase = object : RoomDatabase.Callback() {
        override fun  onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                database.estateDao().addEstateList(FakeEstateApi.getEstateList())
            }
        }
    }

}