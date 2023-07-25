package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.api.FakeEstateApi
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.ui.MainActivityViewModel
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class MainActivityViewModelTest {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var estateRepository: EstateRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        estateRepository = mockk()
        mainActivityViewModel = MainActivityViewModel(estateRepository)
    }

    @Test
    fun setSelectedEstateTest() {
        // Given
        val expectedEstate = FakeEstateApi.getEstateList()[1]
        every { estateRepository.setSelectedEstate(expectedEstate) } just runs

        // When
        mainActivityViewModel.setSelectedEstate(expectedEstate)

        // Then
        verify { estateRepository.setSelectedEstate(expectedEstate) }
    }

}