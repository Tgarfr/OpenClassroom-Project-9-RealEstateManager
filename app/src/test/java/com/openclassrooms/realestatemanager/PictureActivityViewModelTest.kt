package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.repository.PictureRepository
import com.openclassrooms.realestatemanager.ui.PictureActivityViewModel
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

class PictureActivityViewModelTest {

    private lateinit var pictureActivityViewModel: PictureActivityViewModel
    private lateinit var pictureRepository: PictureRepository

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        pictureRepository = mockk()
        pictureActivityViewModel = PictureActivityViewModel(pictureRepository)
    }

    @Test
    fun getPictureListByEstateIdLiveDataTest() {
        // Given
        val expectedEstateId = 3L
        val originPictureList = testPictureList
        val expectedPictureList = originPictureList.filter { picture -> picture.estateId == expectedEstateId }
        every { pictureRepository.getPictureListByEstateIdLiveData(expectedEstateId) } returns MutableLiveData(expectedPictureList)

        // When
        val actualPictureListLiveData = pictureActivityViewModel.getPictureListByEstateIdLiveData(expectedEstateId)

        // Then
        val actualPictureList = LiveDataTestUtils.getValue(actualPictureListLiveData)
        Assert.assertEquals(expectedPictureList, actualPictureList)
    }

    @Test
    fun getPictureByIdTest() {
        // Given
        val expectedPictureList = testPictureList
        val expectedPicture = expectedPictureList[1]
        every { pictureRepository.getPictureListLiveData() } returns MutableLiveData(testPictureList)

        // When
        val actualPicture = pictureActivityViewModel.getPictureById(expectedPicture.id)

        // Then
        Assert.assertEquals(expectedPicture, actualPicture)
    }

    @Test
    fun deletePictureTest() {
        // Given
        val expectedDeletedPicture = testPictureList[2]
        every { pictureRepository.deletePicture(expectedDeletedPicture) } just runs

        // When
        pictureActivityViewModel.deletePicture(expectedDeletedPicture)

        // Then
        verify { pictureRepository.deletePicture(expectedDeletedPicture) }
    }

    private val testPictureList = listOf(
        Picture(1,3, "uri 1", "Description 1"),
        Picture(3,1, "uri 3", "Description 3"),
        Picture(5,3, "uri 5", "Description 5"),
        Picture(8,30, "uri 8", "Description 8")
    )

}