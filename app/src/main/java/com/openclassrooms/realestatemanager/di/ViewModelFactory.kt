package com.openclassrooms.realestatemanager.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.ui.MainActivityViewModel

object ViewModelFactory: ViewModelProvider.Factory {

    private val estateRepository: EstateRepository = Injection.estateRepository

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(estateRepository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }

}