package com.openclassrooms.realestatemanager

import android.location.Location
import com.openclassrooms.realestatemanager.api.GeocodingApi
import com.openclassrooms.realestatemanager.repository.GeocodingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GeocodingRepositoryTest {

    private lateinit var geocodingApi: GeocodingApi

    private lateinit var geocodingRepository: GeocodingRepository

    @Before
    fun setUp() {
        geocodingApi = mockk()
        geocodingRepository = GeocodingRepository(geocodingApi)
    }

    @Test
    fun geocodeAddressTest() {
        // Given
        val expectedAddressString = "Address Test"
        val expectedLocation: Location = mockk()
        val expectedLatitude = 1.0
        val expectedLongitude = 2.0
        every { expectedLocation.latitude } returns expectedLatitude
        every { expectedLocation.longitude } returns expectedLongitude
        every { geocodingApi.geocodeAddress(expectedAddressString) } returns expectedLocation

        // When
        val actualLocation = geocodingRepository.geocodeAddress(expectedAddressString)

        // Then
        verify { geocodingApi.geocodeAddress(expectedAddressString) }
        Assert.assertEquals(expectedLatitude, actualLocation?.latitude)
        Assert.assertEquals(expectedLongitude, actualLocation?.longitude)
    }

    @Test
    fun isInSectorTest() {
        // Given
        val expectedSectorString = "Address Test"
        val expectedLatitude = 47.796565789559914
        val expectedLongitude = 2.138827720163123
        val expectedBoolean = true
        every { geocodingApi.isInSector(expectedSectorString, expectedLatitude, expectedLongitude) } returns expectedBoolean

        // When
        val actualBoolean = geocodingRepository.isInSector(expectedSectorString, expectedLatitude, expectedLongitude)

        // Then
        verify { geocodingApi.isInSector(expectedSectorString, expectedLatitude, expectedLongitude) }
        Assert.assertEquals(expectedBoolean, actualBoolean)
    }

}