package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.GeocodingRepository
import com.openclassrooms.realestatemanager.repository.PictureRepository
import com.openclassrooms.realestatemanager.repository.SearchRepository
import com.openclassrooms.realestatemanager.ui.*

class ViewModelFactory private constructor(context: Context): ViewModelProvider.Factory {

    private val estateRepository: EstateRepository = Injection.getInstance(context).estateRepository
    private val pictureRepository: PictureRepository = Injection.getInstance(context).pictureRepository
    private val searchRepository: SearchRepository = Injection.getInstance(context).searchRepository
    private val geocodingRepository: GeocodingRepository = Injection.getInstance(context).geocodingRepository
    private val agentRepository: AgentRepository = Injection.getInstance(context).agentRepository

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
            return EstateListFragmentViewModel(estateRepository, searchRepository, pictureRepository, geocodingRepository) as T
        }
        if (modelClass.isAssignableFrom(EstateSheetFragmentViewModel::class.java)) {
            return EstateSheetFragmentViewModel(estateRepository, pictureRepository, agentRepository) as T
        }
        if (modelClass.isAssignableFrom(EstateEditFragmentViewModel::class.java)) {
            return EstateEditFragmentViewModel(estateRepository, pictureRepository, geocodingRepository, agentRepository) as T
        }
        if (modelClass.isAssignableFrom(PictureActivityViewModel::class.java)) {
            return PictureActivityViewModel(pictureRepository) as T
        }
        if (modelClass.isAssignableFrom(LoanSimulatorActivityViewModel::class.java)) {
            return LoanSimulatorActivityViewModel() as T
        }
        if (modelClass.isAssignableFrom(MapFragmentViewModel::class.java)) {
            return MapFragmentViewModel(estateRepository) as T
        }
        if (modelClass.isAssignableFrom(EstateListFilterFragmentViewModel::class.java)) {
            return EstateListFilterFragmentViewModel(searchRepository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }

}