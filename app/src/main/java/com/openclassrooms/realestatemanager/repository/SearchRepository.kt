package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.model.SearchCriteria

class SearchRepository {

    private val searchCriteriaLiveData: MutableLiveData<List<SearchCriteria>> = MutableLiveData()

    fun getSearchCriteriaLiveData(): LiveData<List<SearchCriteria>> = searchCriteriaLiveData

    fun setSearchCriteriaLiveData(searchCriteria: List<SearchCriteria>) { searchCriteriaLiveData.value = searchCriteria }

}