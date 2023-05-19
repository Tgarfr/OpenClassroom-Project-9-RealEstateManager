package com.openclassrooms.realestatemanager

import org.junit.Assert
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun convertDollarToEuroTest() {
        // Given
        val dollar = 1234
        val expectedEuro = 1002

        // When
        val actualEuro = Utils.convertDollarToEuro(dollar)

        // Then
        Assert.assertEquals(expectedEuro, actualEuro)
    }

    @Test
    fun convertEuroToDollarTest() {
        // Given
        val euro = 1234
        val expectedDollar = 1520

        // When
        val actualDollar = Utils.convertEuroToDollar(euro)

        // Then
        Assert.assertEquals(expectedDollar, actualDollar)
    }

    @Test
    fun getTodayDate() {
        // Given
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val expectedDate = dateFormat.format(Date())

        // When
        val actualDate = Utils.getTodayDate()

        // Then
        Assert.assertEquals(expectedDate, actualDate)
    }
}