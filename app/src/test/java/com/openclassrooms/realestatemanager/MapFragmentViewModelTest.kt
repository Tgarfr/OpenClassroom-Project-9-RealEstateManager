package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.api.FakeEstateApi
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.ui.MapFragmentViewModel
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapFragmentViewModelTest {

    private lateinit var mapFragmentViewModel: MapFragmentViewModel
    private lateinit var estateRepository: EstateRepository

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        estateRepository = mockk()
        mapFragmentViewModel = MapFragmentViewModel(estateRepository)
    }

    @Test
    fun getEstateListLiveDataTest() {
        // Given
        val expectedEstateList = FakeEstateApi.getEstateList()
        every { estateRepository.getEstateListLiveData() } returns MutableLiveData(expectedEstateList)

        // When
        val actualEstateListLiveData = mapFragmentViewModel.getEstateListLiveData()

        // Then
        val actualEstateList = LiveDataTestUtils.getValue(actualEstateListLiveData)
        Assert.assertEquals(expectedEstateList, actualEstateList)
    }

    @Test
    fun getEstateFromTest() {
        // Given
        val expectedEstateList = FakeEstateApi.getEstateList()
        val expectedEstate =  expectedEstateList[1]
        every { estateRepository.getEstateListLiveData() } returns MutableLiveData(expectedEstateList)

        // When
        val actualEstate = mapFragmentViewModel.getEstateFrom(expectedEstate.id)

        // Then
        Assert.assertEquals(expectedEstate, actualEstate)
    }


}