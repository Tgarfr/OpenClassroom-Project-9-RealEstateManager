package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.pow

class LoanSimulatorActivityViewModel: ViewModel() {

    private var monthlyPaymentLiveData: MutableLiveData<Double> = MutableLiveData(0.0)
    private var totalInterestsLiveData: MutableLiveData<Double> = MutableLiveData(0.0)
    private var totalPayment: MutableLiveData<Double> = MutableLiveData(0.0)

    fun getMonthlyPaymentLiveData() = monthlyPaymentLiveData

    fun getTotalInterestsLiveData() = totalInterestsLiveData

    fun getTotalPaymentLiveData() = totalPayment

    fun calculateTotalCoast(years: Int, amount: Double, rate: Double, bring: Double ) {

        val amountBorrowed = amount - bring
        val numberOfMonthlyPayments = years * 12
        val periodicRate = (1.0 + rate).pow(1.0 / 12.0) - 1

        val blockOfCalculation = ( 1 + periodicRate).pow(numberOfMonthlyPayments)
        val monthlyPayment = ( amountBorrowed * periodicRate * blockOfCalculation ) / ( blockOfCalculation - 1  )
        monthlyPaymentLiveData.value = monthlyPayment

        val totalInterests = ( monthlyPayment * numberOfMonthlyPayments ) - amountBorrowed
        totalInterestsLiveData.value = totalInterests

        totalPayment.value = amountBorrowed + totalInterests

    }

}