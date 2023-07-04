package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.ui.*

class ViewModelFactory private constructor(context: Context): ViewModelProvider.Factory {

    private val estateRepository: EstateRepository = Injection.getInstance(context).estateRepository

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelProvider.Factory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(context).also { INSTANCE = it }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(estateRepository) as T
        }
        if (modelClass.isAssignableFrom(EstateListFragmentViewModel::class.java)) {
            return EstateListFragmentViewModel(estateRepository) as T
        }
        if (modelClass.isAssignableFrom(EstateSheetFragmentViewModel::class.java)) {
            return EstateSheetFragmentViewModel(estateRepository) as T
        }
        if (modelClass.isAssignableFrom(EstateEditFragmentViewModel::class.java)) {
            return EstateEditFragmentViewModel(estateRepository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }

}