package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import java.text.Format
import java.text.NumberFormat
import java.util.Locale

class LoanSimulatorActivity: AppCompatActivity() {

    private val currencyFormat: Format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    private lateinit var viewModel: LoanSimulatorActivityViewModel
    private lateinit var amountEditText: EditText
    private lateinit var yearsEditText: EditText
    private lateinit var rateEditText: EditText
    private lateinit var bringEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator_loan)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoanSimulatorActivityViewModel::class.java]

        amountEditText = findViewById(R.id.activity_simulator_loan_amount)
        yearsEditText = findViewById(R.id.activity_simulator_loan_years)
        rateEditText = findViewById(R.id.activity_simulator_loan_rate)
        bringEditText = findViewById(R.id.activity_simulator_loan_bring)
        val monthlyPaymentTextView = findViewById<TextView>(R.id.activity_simulator_loan_monthly_payment)
        val totalInterestsTextView = findViewById<TextView>(R.id.activity_simulator_loan_total_interests)
        val totalPaymentTextView = findViewById<TextView>(R.id.activity_simulator_loan_total_payment)

        viewModel.getMonthlyPaymentLiveData().observe(this)
            { value -> monthlyPaymentTextView.text = currencyFormat.format(value)  }
        viewModel.getTotalInterestsLiveData().observe(this)
            { value -> totalInterestsTextView.text = currencyFormat.format(value)  }
        viewModel.getTotalPaymentLiveData().observe(this)
            { value -> totalPaymentTextView.text = currencyFormat.format(value)  }

        findViewById<Button>(R.id.activity_simulator_button_calculate).setOnClickListener {
            if (dataIsCompleted()) {

                val amount = amountEditText.text.toString().toDouble()
                val years = yearsEditText.text.toString().toInt()
                val rate = rateEditText.text.toString().toDouble() / 100

                val bringText = bringEditText.text.toString()
                var bring = 0.0
                if (bringText.isNotEmpty()) {
                    bring = bringText.toDouble()
                }

                viewModel.calculate(years, amount, rate, bring).toString()
            }
        }

    }

    private fun dataIsCompleted():Boolean {
        when {
            amountEditText.text.toString().isEmpty()
                -> Toast.makeText(this, "Amount is required", Toast.LENGTH_SHORT).show()
            yearsEditText.text.toString().isEmpty()
                -> Toast.makeText(this, "Duration is required", Toast.LENGTH_SHORT).show()
            rateEditText.text.toString().isEmpty()
                -> Toast.makeText(this, "Rate is required", Toast.LENGTH_SHORT).show()
            else -> return true
        }
        return false
    }

}