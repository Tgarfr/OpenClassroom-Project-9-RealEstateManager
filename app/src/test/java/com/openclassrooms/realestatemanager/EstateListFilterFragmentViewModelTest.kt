package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.model.SearchCriteria
import com.openclassrooms.realestatemanager.repository.SearchRepository
import com.openclassrooms.realestatemanager.ui.EstateListFilterFragmentViewModel
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EstateListFilterFragmentViewModelTest {

    private lateinit var estateListFilterFragmentViewModel: EstateListFilterFragmentViewModel
    private lateinit var searchRepository: SearchRepository

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        searchRepository = mockk()
        estateListFilterFragmentViewModel = EstateListFilterFragmentViewModel(searchRepository)
    }

    @Test
    fun getSearchCriteriaLiveDataTest() {
        // Given
        val expectedSearchCriteria = testSearchCriteria
        every { searchRepository.getSearchCriteriaLiveData() } returns MutableLiveData(expectedSearchCriteria)

        // When
        val actualSearchCriteriaLiveData = estateListFilterFragmentViewModel.getSearchCriteriaLiveData()

        // Then
        val actualSearchCriteria = LiveDataTestUtils.getValue(actualSearchCriteriaLiveData)
        Assert.assertEquals(expectedSearchCriteria, actualSearchCriteria)
    }

    @Test
    fun setSearchCriteriaTest() {
        // Given
        val expectedSearchCriteria = testSearchCriteria
        every { searchRepository.setSearchCriteria(expectedSearchCriteria) } just runs

        // When
        estateListFilterFragmentViewModel.setSearchCriteria(expectedSearchCriteria)

        // Then
        verify { searchRepository.setSearchCriteria(expectedSearchCriteria) }
    }

    private val testSearchCriteria = listOf(
        SearchCriteria.MaxNumberOfPictures(3),
        SearchCriteria.MinParcDistance(20),
        SearchCriteria.MaxNumberOfRooms(2)
    )

}