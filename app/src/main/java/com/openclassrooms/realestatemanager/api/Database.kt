package com.openclassrooms.realestatemanager.api

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.model.Estate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@androidx.room.Database(entities = [Estate::class], version = 1)
abstract class Database: RoomDatabase() {

    abstract fun estateDao(): EstateDao

    companion object {
        private const val DATABASE_NAME: String = "RealEstateManagerDatabase"

        @Volatile
        private var INSTANCE: Database? = null

        fun getInstance(context: Context): Database =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: databaseBuilder(context.applicationContext, Database::class.java, DATABASE_NAME)
                    .addCallback(prepopulateDatabase)
                    .build().also { INSTANCE = it }
            }

        private val prepopulateDatabase = object : Callback() {
            override fun  onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.estateDao()?.addEstateList(FakeEstateApi().getEstateList())
                }
            }
        }
    }

}