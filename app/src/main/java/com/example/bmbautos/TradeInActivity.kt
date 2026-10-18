package com.example.bmbautos

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class TradeInActivity : AppCompatActivity() {

    private lateinit var etMake: EditText
    private lateinit var etModel: EditText
    private lateinit var etYear: EditText
    private lateinit var etPrice: EditText
    // CHANGED: Now an AutoCompleteTextView instead of Spinner
    private lateinit var spinnerType: AutoCompleteTextView
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_in)

        etMake = findViewById(R.id.etMake)
        etModel = findViewById(R.id.etModel)
        etYear = findViewById(R.id.etYear)
        etPrice = findViewById(R.id.etPrice)
        spinnerType = findViewById(R.id.spinnerType)
        btnSubmit = findViewById(R.id.btnSubmit)
        progressBar = findViewById(R.id.progressBar)

        // CHANGED: Setup the modern dropdown menu
        val types = arrayOf("Sedan", "Hatchback", "SUV", "Bakkie", "Coupe")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        spinnerType.setAdapter(adapter)

        btnSubmit.setOnClickListener {
            val make = etMake.text.toString().trim()
            val model = etModel.text.toString().trim()
            val yearStr = etYear.text.toString().trim()
            val price = etPrice.text.toString().trim()
            // CHANGED: Get text from AutoCompleteTextView
            val type = spinnerType.text.toString()

            if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || price.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val year = yearStr.toIntOrNull() ?: 2021
            submitTradeIn(make, model, year, price, type)
        }
    }

    private fun submitTradeIn(make: String, model: String, year: Int, price: String, type: String) {
        progressBar.visibility = View.VISIBLE
        btnSubmit.text = ""

        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val request = TradeInRequest(make, model, year, price, type)
                val response = ApiClient.getApi().createTradeIn("Bearer $token", request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@TradeInActivity, "Vehicle Submitted Successfully!", Toast.LENGTH_LONG).show()
                    finish() // Go back to dashboard
                } else {
                    Toast.makeText(this@TradeInActivity, "Failed: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TradeInActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                btnSubmit.text = "Submit for Evaluation"
            }
        }
    }
}