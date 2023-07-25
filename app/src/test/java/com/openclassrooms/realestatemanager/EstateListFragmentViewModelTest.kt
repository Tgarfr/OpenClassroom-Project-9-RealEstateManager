package com.openclassrooms.realestatemanager

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.api.FakeEstateApi
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateItem
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.model.SearchCriteria
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.GeocodingRepository
import com.openclassrooms.realestatemanager.repository.PictureRepository
import com.openclassrooms.realestatemanager.repository.SearchRepository
import com.openclassrooms.realestatemanager.ui.EstateListFragmentViewModel
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils3rdValue
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class EstateListFragmentViewModelTest {

    private lateinit var estateListFragmentViewModel: EstateListFragmentViewModel

    private lateinit var estateRepository: EstateRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var pictureRepository: PictureRepository
    private lateinit var geocodingRepository: GeocodingRepository

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        estateRepository = mockk()
        searchRepository = mockk()
        pictureRepository = mockk()
        geocodingRepository = mockk()
        estateListFragmentViewModel = EstateListFragmentViewModel(estateRepository, searchRepository, pictureRepository, geocodingRepository)
    }

    @Test
    fun getEstateItemListLiveDataTest() = runBlocking {
        // Given
        val expectedEstateList = testEstateList
        val expectedSearchCriteria = testSearchCriteria
        val uriMock: Uri = mockk()
        val expectedPictureList = testPictureList
        expectedPictureList.forEachIndexed { index: Int, picture: Picture ->
            every { picture.id } returns index.toLong()
            every { picture.getUri() } returns uriMock
            if (index == 1) {
                every { picture.estateId } returns 0L
            } else {
                every { picture.estateId } returns 1L
            }
        }

        every { searchRepository.getSearchCriteriaLiveData() } returns MutableLiveData(expectedSearchCriteria)
        every { pictureRepository.getPictureListLiveData() } returns MutableLiveData(expectedPictureList)
        every { estateRepository.getEstateListLiveData() } returns MutableLiveData(expectedEstateList)

        val sectorString = (testSearchCriteria.find { searchCriteria -> searchCriteria is SearchCriteria.Sector } as SearchCriteria.Sector).value
        expectedEstateList.forEach { estate ->
            val estateLatitude = estate.latitude
            val estateLongitude = estate.longitude
            if (estateLatitude != null && estateLongitude != null) {
                val isInSector = when(estate.id) {
                    0L -> false
                    1L-> true
                    2L -> false
                    else -> false
                }
                every { geocodingRepository.isInSector(sectorString, estateLatitude, estateLongitude) } returns isInSector
            }
        }

        val expectedFilteredEstateList = listOf(testEstateList[1])
        val expectedFilteredEstateItemList = expectedFilteredEstateList.map { estate -> EstateItem(estate, uriMock) }

        // When
        val estateItemListLiveData = estateListFragmentViewModel.getEstateItemListLiveData()

        // Then
        val actualEstateItemList = LiveDataTestUtils3rdValue.getValue(estateItemListLiveData)
        Assert.assertEquals(expectedFilteredEstateItemList, actualEstateItemList)
    }

    @Test
    fun getSelectedEstateLiveDataTest() {
        // Given
        val expectedEstate = FakeEstateApi.getEstateList()[1]
        every { estateRepository.getSelectedEstateLiveData() } returns MutableLiveData(expectedEstate)

        // When
        val actualEstateLiveData = estateListFragmentViewModel.getSelectedEstateLiveData()

        // Then
        val actualEstate = LiveDataTestUtils.getValue(actualEstateLiveData)
        Assert.assertEquals(actualEstate, expectedEstate)
    }

    private val testEstateList = listOf(
        Estate(
            id = 0L,
            type = Estate.Type.DUPLEX,
            price = 10.0,
            surface = 20F,
            numberOfRooms = 30,
            numberOfBathrooms = 40,
            numberOfBedrooms = 50,
            schoolDistance = 60,
            shopDistance = 70,
            parkDistance = 80,
            description = "DESCRIPTION",
            houseNumber = 1,
            street = "Oak street",
            zipCode = "10001",
            city = "New York",
            country = "United States",
            additionalAddress = "Apt 6/7A",
            latitude = 0.1,
            longitude = 0.2,
            status = Estate.Status.AVAILABLE,
            entryDate = Calendar.getInstance().timeInMillis,
            saleDate = Calendar.getInstance().timeInMillis,
            agentId = 2L),
        Estate(
            id = 1L,
            type = Estate.Type.HOUSE,
            price = 15.00,
            surface = 25F,
            numberOfRooms = 35,
            numberOfBathrooms = 45,
            numberOfBedrooms = 55,
            schoolDistance = 65,
            shopDistance = 75,
            parkDistance = 85,
            description = "DESCRIPTION",
            houseNumber = 1,
            street = "Oak street",
            zipCode = "10001",
            city = "New York",
            country = "United States",
            additionalAddress = "Apt 6/7A",
            latitude = 1.1,
            longitude = 1.2,
            status = Estate.Status.AVAILABLE,
            entryDate = Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, 5) }.timeInMillis,
            saleDate = Calendar.getInstance().apply { add(Calendar.MONTH, 5) }.timeInMillis,
            agentId = 2L),
        Estate(
            id = 2L,
            type = Estate.Type.PENTHOUSE,
            price = 19.0,
            surface = 29F,
            numberOfRooms = 39,
            numberOfBathrooms = 49,
            numberOfBedrooms = 59,
            schoolDistance = 69,
            shopDistance = 79,
            parkDistance = 89,
            description = "DESCRIPTION",
            houseNumber = 1,
            street = "Oak street",
            zipCode = "10001",
            city = "New York",
            country = "United States",
            additionalAddress = "Apt 6/7A",
            latitude = 2.1,
            longitude = 2.2,
            status = Estate.Status.AVAILABLE,
            entryDate = Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, 9) }.timeInMillis,
            saleDate = Calendar.getInstance().apply { add(Calendar.MONTH, 9) }.timeInMillis,
            agentId = 2L
        )
    )

    private val testSearchCriteria = listOf(
        SearchCriteria.MinPrice(14.0),
        SearchCriteria.MaxPrice(16.0),
        SearchCriteria.MinSurface(24),
        SearchCriteria.MaxSurface(26),
        SearchCriteria.MinNumberOfRooms(34),
        SearchCriteria.MaxNumberOfRooms(36),
        SearchCriteria.MinNumberOfBathrooms(44),
        SearchCriteria.MaxNumberOfBathrooms(46),
        SearchCriteria.MinNumberOfBedrooms(54),
        SearchCriteria.MaxNumberOfBedrooms(56),
        SearchCriteria.MinSchoolDistance(64),
        SearchCriteria.MaxSchoolDistance(66),
        SearchCriteria.MinShopDistance(74),
        SearchCriteria.MaxShopDistance(76),
        SearchCriteria.MinParcDistance(84),
        SearchCriteria.MaxParcDistance(86),
        SearchCriteria.MinNumberOfPictures(3),
        SearchCriteria.MaxNumberOfPictures(4),
        SearchCriteria.Type(Estate.Type.HOUSE),
        SearchCriteria.PutOnTheMarketSince(5),
        SearchCriteria.SoldSince(5),
        SearchCriteria.Sector("sector"),
    )

    private val testPictureList = listOf<Picture>(
        mockk("Picture 0"),
        mockk("Picture 1"),
        mockk("Picture 2"),
        mockk("Picture 3"),
        mockk("Picture 4")
    )

}