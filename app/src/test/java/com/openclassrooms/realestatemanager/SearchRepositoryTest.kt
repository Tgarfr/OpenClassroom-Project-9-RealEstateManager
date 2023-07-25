package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.model.SearchCriteria
import com.openclassrooms.realestatemanager.repository.SearchRepository
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SearchRepositoryTest {

    private val searchRepository = SearchRepository()

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun setAndGetSearchCriteriaLiveDataTest() {
        // Given
        val expectedSearchCriteria = searchCriteria

        // When
        searchRepository.setSearchCriteria(searchCriteria)
        val actualSearchCriteriaLiveData = searchRepository.getSearchCriteriaLiveData()

        // Then
        val actualSearchCriteria = LiveDataTestUtils.getValue(actualSearchCriteriaLiveData)
        Assert.assertEquals(expectedSearchCriteria, actualSearchCriteria)
    }

    private val searchCriteria = listOf(
        SearchCriteria.MaxNumberOfPictures(3),
        SearchCriteria.MinParcDistance(20),
        SearchCriteria.MaxNumberOfRooms(2)
    )

}