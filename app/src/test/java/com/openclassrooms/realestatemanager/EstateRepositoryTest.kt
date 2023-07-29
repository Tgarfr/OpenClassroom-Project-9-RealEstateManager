package com.openclassrooms.realestatemanager

import android.database.Cursor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.api.EstateApi
import com.openclassrooms.realestatemanager.api.FakeEstateApi
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EstateRepositoryTest {

    private lateinit var estateRepository: EstateRepository
    private lateinit var estateApi: EstateApi
    private val fakeEstateList = FakeEstateApi.getEstateList()

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        estateApi = mockk()
        every { estateApi.getEstateListFlow() } returns flowOf(fakeEstateList)
        estateRepository = EstateRepository(estateApi)
    }

    @Test
    fun getEstateListLiveDataTest() {
        // Given
        val expectedEstateList = fakeEstateList

        // When
        val actualEstateListLiveData = estateRepository.getEstateListLiveData()

        // Then
        val actualEstateList = LiveDataTestUtils.getValue(actualEstateListLiveData)
        Assert.assertEquals(expectedEstateList, actualEstateList)
    }

    @Test
    fun getEstateListCursorTest() {
        // Given
        val expectedEstateListCursor: Cursor = mockk()
        every { estateRepository.getEstateListCursor() } returns expectedEstateListCursor

        // When
        val actualEstateListCursor = estateRepository.getEstateListCursor()

        // Then
        Assert.assertEquals(expectedEstateListCursor, actualEstateListCursor)
    }

    @Test
    fun addEstateTest() {
        // Given
        val expectedEstate = testEstate
        every { estateApi.addEstate(expectedEstate) } returns expectedEstate.id

        // When
        val actualExpectedEstateId = estateRepository.addEstate(expectedEstate)

        // Then
        verify { estateApi.addEstate(expectedEstate) }
        Assert.assertEquals(expectedEstate.id, actualExpectedEstateId)
    }

    @Test
    fun updateEstateTest() {
        // Given
        val expectedEstate = fakeEstateList[1]
        val expectedNumberOfLine = 1
        every { estateApi.updateEstate(expectedEstate) } returns expectedNumberOfLine

        // When
        val actualFeedback = estateRepository.updateEstate(expectedEstate)

        // Then
        verify { estateApi.updateEstate(expectedEstate) }
        Assert.assertEquals(expectedNumberOfLine, actualFeedback)
    }

    @Test
    fun setAndGetSelectedEstateLiveDataTest() {
        // Given
        val expectedEstate = fakeEstateList[2]

        // When
        estateRepository.setSelectedEstate(expectedEstate)
        val actualEstateLivedata = estateRepository.getSelectedEstateLiveData()

        // Then
        val actualEstate = LiveDataTestUtils.getValue(actualEstateLivedata)
        Assert.assertEquals(expectedEstate, actualEstate)

    }

    private val testEstate = Estate(
    id = 22,
    type = Estate.Type.HOUSE,
    price = 150020.30,
    surface = 120F,
    numberOfRooms = 5,
    numberOfBathrooms = 5,
    numberOfBedrooms = 5,
    schoolDistance = 5,
    shopDistance = 5,
    parkDistance = 5,
    description = "DESCRIPTION",
    houseNumber = 1,
    street = "Oak street",
    zipCode = "10001",
    city = "New York",
    country = "United States",
    additionalAddress = "Apt 6/7A",
    latitude = 1.0,
    longitude = 2.0,
    status = Estate.Status.AVAILABLE,
    entryDate = 1680307200000,
    saleDate = null,
    agentId = 2L
    )

}