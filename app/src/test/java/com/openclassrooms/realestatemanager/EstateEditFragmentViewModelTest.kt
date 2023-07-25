package com.openclassrooms.realestatemanager

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.GeocodingRepository
import com.openclassrooms.realestatemanager.repository.PictureRepository
import com.openclassrooms.realestatemanager.ui.EstateEditFragmentViewModel
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EstateEditFragmentViewModelTest {

    private lateinit var estateEditFragmentViewModel: EstateEditFragmentViewModel

    private lateinit var estateRepository: EstateRepository
    private lateinit var pictureRepository: PictureRepository
    private lateinit var geocodingRepository: GeocodingRepository
    private lateinit var agentRepository: AgentRepository

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        estateRepository = mockk()
        pictureRepository = mockk()
        geocodingRepository = mockk()
        agentRepository = mockk()
        estateEditFragmentViewModel = EstateEditFragmentViewModel(estateRepository, pictureRepository, geocodingRepository, agentRepository)
    }

    @Test
    fun setNewEditedEstateAndGetPictureListLiveDataTest() {
        // Given
        val expectedEstateId = 3L
        estateEditFragmentViewModel.setNewEditedEstate()

        // When
        val actualPictureListLiveData = estateEditFragmentViewModel.getPictureListLiveData(expectedEstateId)

        // Then
        val actualPictureList = LiveDataTestUtils.getValue(actualPictureListLiveData)
        Assert.assertEquals(listOf<Picture>(), actualPictureList)
    }

    @Test
    fun getEditedEstateLiveDataAndGetPictureListLiveDataTest() {
        // Given
        val expectedEstate = testEstate
        val expectedEstateId = expectedEstate.id
        val expectedPictureList = testPictureList.filter { picture -> picture.estateId == expectedEstateId }
        every { estateEditFragmentViewModel.getEditedEstateLiveData() } returns MutableLiveData(expectedEstate)

        every { pictureRepository.getPictureListByEstateIdLiveData(expectedEstateId) } returns MutableLiveData(expectedPictureList)

        // When
        val actualEstateLiveData = estateEditFragmentViewModel.getEditedEstateLiveData()
        val actualPictureListLiveData = estateEditFragmentViewModel.getPictureListLiveData(expectedEstateId)

        // Then
        val actualEstate = LiveDataTestUtils.getValue(actualEstateLiveData)
        Assert.assertEquals(expectedEstate, actualEstate)
        val actualPictureList = LiveDataTestUtils.getValue(actualPictureListLiveData)
        Assert.assertEquals(expectedPictureList, actualPictureList)
    }

    @Test
    fun getAgentListLiveDataTest() {
        // Given
        val expectedAgentList = testAgentList
        every { agentRepository.getAgentListLiveData() } returns MutableLiveData(expectedAgentList)

        // When
        val actualAgentListLiveData = estateEditFragmentViewModel.getAgentListLiveData()

        // Then
        val actualAgentList = LiveDataTestUtils.getValue(actualAgentListLiveData)
        Assert.assertEquals(expectedAgentList, actualAgentList)
    }

    @Test
    fun addEstateTest() = runBlocking {
        // Given
        val expectedEstate = testEstate
        every { estateRepository.addEstate(expectedEstate) } returns expectedEstate.id

        // When
        estateEditFragmentViewModel.addEstate(expectedEstate)

        // Then
        verify { estateRepository.addEstate(expectedEstate) }
    }

    @Test
    fun addPictureAndUpdateEstateTest() = runBlocking {
        // Given
        val expectedPicture = testPictureList[1]
        val expectedEstate = testEstate
        every { estateRepository.updateEstate(expectedEstate) } returns 1
        every { pictureRepository.addPictureList(listOf(expectedPicture)) } just runs

        // When
        estateEditFragmentViewModel.addPicture(expectedPicture)
        estateEditFragmentViewModel.updateEstate(expectedEstate)

        // Then
        estateRepository.updateEstate(expectedEstate)
        pictureRepository.addPictureList(listOf(expectedPicture))
    }

    @Test
    fun geocodeAddressTest() {
        // Given
        val expectedAddress = "Expected address"
        val expectedLocation: Location = mockk()
        val expectedLatitude = 1.0
        val expectedLongitude = 2.0
        every { expectedLocation.latitude } returns expectedLatitude
        every { expectedLocation.longitude } returns expectedLongitude
        every { geocodingRepository.geocodeAddress(expectedAddress) } returns expectedLocation

        // When
        val actualLocation = estateEditFragmentViewModel.geocodeAddress(expectedAddress)

        // Then
        verify { geocodingRepository.geocodeAddress(expectedAddress) }
        Assert.assertTrue(actualLocation != null)
        Assert.assertEquals(expectedLatitude, actualLocation?.latitude)
        Assert.assertEquals(expectedLongitude, actualLocation?.longitude)
    }

    private val testEstate = Estate(
        id = 3,
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

    private val testAgentList = listOf(
        Agent(0, "Marc Leblanc"),
        Agent(1, "Eric Dupont"),
        Agent(2, "Elise Duval"),
        Agent(3, "Henri Auger"),
    )

    private val testPictureList = listOf(
        Picture(1,3, "uri 1", "Description 1"),
        Picture(3,1, "uri 3", "Description 3"),
        Picture(5,3, "uri 5", "Description 5"),
        Picture(8,30, "uri 8", "Description 8")
    )

}