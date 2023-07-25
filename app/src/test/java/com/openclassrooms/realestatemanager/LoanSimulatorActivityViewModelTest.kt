package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.ui.LoanSimulatorActivityViewModel
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class LoanSimulatorActivityViewModelTest {

    private val loanSimulatorActivityViewModel = LoanSimulatorActivityViewModel()

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun calculateAndGetResultsLiveData() {
        // Given
        val year = 20
        val amount = 200000.0
        val rate = 0.012
        val bring = 20000.0
        val expectedMonthlyPayment = 843.0
        val expectedTotalInterests = 22424.0
        val expectedTotalPayment = 202424.0

        // When
        loanSimulatorActivityViewModel.calculate(year, amount, rate, bring)
        val actualMonthlyPaymentLiveData = loanSimulatorActivityViewModel.getMonthlyPaymentLiveData()
        val actualTotalInterestsLiveData = loanSimulatorActivityViewModel.getTotalInterestsLiveData()
        val actualTotalPaymentLiveData = loanSimulatorActivityViewModel.getTotalPaymentLiveData()

        // Then
        val actualMonthlyPayment: Double? = LiveDataTestUtils.getValue(actualMonthlyPaymentLiveData)
        val actualTotalInterests: Double? = LiveDataTestUtils.getValue(actualTotalInterestsLiveData)
        val actualTotalPayment: Double? = LiveDataTestUtils.getValue(actualTotalPaymentLiveData)
        actualMonthlyPayment?.let { Assert.assertEquals(expectedMonthlyPayment, it, 1.0) }
        actualTotalInterests?.let { Assert.assertEquals(expectedTotalInterests, it, 1.0) }
        actualTotalPayment?.let { Assert.assertEquals(expectedTotalPayment, it, 1.0) }
    }


}