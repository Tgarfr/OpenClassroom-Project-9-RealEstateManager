package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.api.FakeEstateApi
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.PictureRepository
import com.openclassrooms.realestatemanager.ui.EstateSheetFragmentViewModel
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
import java.util.Calendar

class EstateSheetFragmentViewModelTest {

    private lateinit var estateSheetFragmentViewModel: EstateSheetFragmentViewModel
    private lateinit var estateRepository: EstateRepository
    private lateinit var pictureRepository: PictureRepository
    private lateinit var agentRepository: AgentRepository
    private val testEstateList = FakeEstateApi.getEstateList()
    private val testSelectedEstate = testEstateList[1]

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        estateRepository = mockk()
        pictureRepository = mockk()
        agentRepository = mockk()
        every { estateRepository.getEstateListLiveData() } returns MutableLiveData(testEstateList)
        every { estateRepository.getSelectedEstateLiveData() } returns MutableLiveData(testSelectedEstate)

        estateSheetFragmentViewModel = EstateSheetFragmentViewModel(estateRepository, pictureRepository, agentRepository)
    }

    @Test
    fun getSelectedEstateLiveDataTest() {
        // Given
        val expectedEstate = testSelectedEstate

        // When
        val actualEstateLiveData = estateSheetFragmentViewModel.getSelectedEstateLiveData()

        // Then
        val actualEstate = LiveDataTestUtils.getValue(actualEstateLiveData)
        Assert.assertEquals(expectedEstate, actualEstate)
    }

    @Test
    fun getAgentListLiveDataTest() {
        // Given
        val expectedAgentList = testAgentList
        every { agentRepository.getAgentListLiveData() } returns MutableLiveData(expectedAgentList)

        // When
        val actualAgentListLiveData = estateSheetFragmentViewModel.getAgentListLiveData()

        // Then
        val actualAgentList = LiveDataTestUtils.getValue(actualAgentListLiveData)
        Assert.assertEquals(expectedAgentList, actualAgentList)
    }

    @Test
    fun getPictureListLiveDataByEstateIdTest() {
        // Given
        val expectedEstateId = 3L
        val expectedPictureList = testPictureList
        val expectedPictureListByEstateId = expectedPictureList.filter { picture -> picture.estateId == expectedEstateId }
        every { pictureRepository.getPictureListByEstateIdLiveData(expectedEstateId) } returns MutableLiveData(expectedPictureListByEstateId)

        // When
        val actualPictureListLiveData = estateSheetFragmentViewModel.getPictureListLiveDataByEstateId(expectedEstateId)

        // Then
        val actualPictureList = LiveDataTestUtils.getValue(actualPictureListLiveData)
        Assert.assertEquals(expectedPictureListByEstateId, actualPictureList)
    }

    @Test
    fun getLocationStringTestWithoutAdditionalAddress() {
        // Given
        val expectedEstate = testEstateList[1]
        val expectedLocationString = "1 Oak street\nMontauk\n10001\nUnited States"

        // When
        val actualLocationString = estateSheetFragmentViewModel.getLocationString(expectedEstate)

        // Then
        Assert.assertEquals(expectedLocationString, actualLocationString)
    }

    @Test
    fun getLocationStringTestWithAdditionalAddress() {
        // Given
        val expectedEstate = testEstateList[2]
        val expectedLocationString = "1 Oak street\nApt 6/7A\nHampton Bays\n10001\nUnited States"

        // When
        val actualLocationString = estateSheetFragmentViewModel.getLocationString(expectedEstate)

        // Then
        Assert.assertEquals(expectedLocationString, actualLocationString)
    }

    @Test
    fun validateEstateSaleTest() {
        // Given
        val expectedSaleDate = Calendar.getInstance().timeInMillis
        val originEstate = testEstateList[2]
        val expectedEstate = Estate(
            id = originEstate.id,
            type = originEstate.type,
            price = originEstate.price,
            surface = originEstate.surface,
            numberOfRooms = originEstate.numberOfRooms,
            numberOfBathrooms = originEstate.numberOfBathrooms,
            numberOfBedrooms = originEstate.numberOfBedrooms,
            schoolDistance = originEstate.schoolDistance,
            shopDistance = originEstate.shopDistance,
            parkDistance = originEstate.parkDistance,
            description = originEstate.description,
            houseNumber = originEstate.houseNumber,
            street = originEstate.street,
            zipCode = originEstate.zipCode,
            city = originEstate.city,
            country = originEstate.country,
            additionalAddress = originEstate.additionalAddress,
            latitude = originEstate.latitude,
            longitude = originEstate.longitude,
            status = Estate.Status.SOLD,
            entryDate = originEstate.entryDate,
            saleDate = expectedSaleDate,
            agentId = originEstate.agentId
        )
        every { estateRepository.updateEstate(expectedEstate) } returns 1
        every { estateRepository.setSelectedEstate(expectedEstate) } just runs

        // When
        estateSheetFragmentViewModel.validateEstateSale(originEstate, expectedSaleDate)

        // Then
        verify { estateRepository.updateEstate(expectedEstate) }
        verify { estateRepository.setSelectedEstate(expectedEstate) }
    }

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