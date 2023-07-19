package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.SearchCriteria
import com.openclassrooms.realestatemanager.repository.SearchRepository

class EstateListFilterFragmentViewModel(private val searchRepository: SearchRepository): ViewModel() {

    fun getSearchCriteriaLiveData(): LiveData<List<SearchCriteria>> = searchRepository.getSearchCriteriaLiveData()

    fun setSearchCriteriaLiveData(searchCriteria: List<SearchCriteria>) { searchRepository.setSearchCriteriaLiveData(searchCriteria) }

}