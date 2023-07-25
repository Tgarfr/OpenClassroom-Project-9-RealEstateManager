package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.api.PictureDatabaseApi
import com.openclassrooms.realestatemanager.api.PictureStorageApi
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.repository.PictureRepository
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PictureRepositoryTest {

    private lateinit var pictureRepository: PictureRepository

    private lateinit var pictureDatabaseApi: PictureDatabaseApi
    private lateinit var pictureStorageApi: PictureStorageApi

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        pictureDatabaseApi = mockk()
        pictureStorageApi = mockk()
        every { pictureDatabaseApi.getPictureListFlow() } returns flowOf(testPictureList)
        pictureRepository = PictureRepository(pictureDatabaseApi, pictureStorageApi)
    }

    @Test
    fun getPictureListLiveDataTest() {
        // Given
        val expectedPictureList = testPictureList

        // When
        val actualPictureListLiveData = pictureRepository.getPictureListLiveData()

        // Then
        val actualPictureList = LiveDataTestUtils.getValue(actualPictureListLiveData)
        Assert.assertEquals(expectedPictureList, actualPictureList)
    }

    @Test
    fun getPictureListByEstateIdLiveDataTest() {
        // Given
        val expectedEstateId = 3L
        val expectedPictureList = testPictureList.filter { picture -> picture.estateId == expectedEstateId }

        // When
        val actualPictureListLiveData = pictureRepository.getPictureListByEstateIdLiveData(expectedEstateId)

        // Then
        val actualPictureList = LiveDataTestUtils.getValue(actualPictureListLiveData)
        Assert.assertEquals(expectedPictureList, actualPictureList)
    }

    @Test
    fun addPictureListTest() {
        // Given
        val expectedPictureList = testPictureList
        val expectedUri = "expected uri"
        every { pictureStorageApi.addPicture(any()) } answers {params ->
            val picture = params.invocation.args[0] as Picture
            Picture(picture.id, picture.estateId, expectedUri, picture.description  )
        }
        expectedPictureList.forEach { expectedPicture ->
            every { pictureDatabaseApi.addPicture(Picture(expectedPicture.id, expectedPicture.estateId, expectedUri, expectedPicture.description  )) } just runs
        }

        // When
        pictureRepository.addPictureList(expectedPictureList)

        // Then
        expectedPictureList.forEach { expectedPicture ->
            pictureStorageApi.addPicture(expectedPicture)
            pictureDatabaseApi.addPicture(Picture(expectedPicture.id, expectedPicture.estateId, expectedUri, expectedPicture.description  ))
        }
    }

    @Test
    fun deletePictureTest() {
        // Given
        val expectedPicture = testPictureList[3]
        every { pictureStorageApi.deletePicture(expectedPicture) } just runs
        every { pictureDatabaseApi.deletePicture(expectedPicture) } just runs

        // When
        pictureRepository.deletePicture(expectedPicture)

        // Then
        pictureStorageApi.deletePicture(expectedPicture)
        pictureDatabaseApi.deletePicture(expectedPicture)
    }

    private val testPictureList = listOf(
        Picture(1,3, "uri 1", "Description 1"),
        Picture(3,1, "uri 3", "Description 3"),
        Picture(5,3, "uri 5", "Description 5"),
        Picture(8,30, "uri 8", "Description 8"))

}