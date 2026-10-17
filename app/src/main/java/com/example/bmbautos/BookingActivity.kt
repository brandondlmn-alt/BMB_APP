package com.example.bmbautos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class BookingActivity : AppCompatActivity() {

    private lateinit var etVehicle: EditText
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var spinnerService: Spinner
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        etVehicle = findViewById(R.id.etVehicle)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        spinnerService = findViewById(R.id.spinnerService)
        btnSubmit = findViewById(R.id.btnSubmit)
        progressBar = findViewById(R.id.progressBar)

        // Setup the dropdown spinner
        val services = arrayOf(
            "Pre-Sale Evaluation", "Oil & Filter Service",
            "Brake Inspection", "Major Diagnostic", "Suspension Check"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, services)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerService.adapter = adapter

        // Show Calendar popup when Date field is clicked
        etDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                // Format as YYYY-MM-DD for the API
                val formattedMonth = String.format("%02d", m + 1)
                val formattedDay = String.format("%02d", d)
                etDate.setText("$y-$formattedMonth-$formattedDay")
            }, year, month, day)
            datePicker.show()
        }

        // Show Time popup when Time field is clicked
        etTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, { _, h, m ->
                // Format as HH:MM:00 for the API
                val formattedHour = String.format("%02d", h)
                val formattedMinute = String.format("%02d", m)
                etTime.setText("$formattedHour:$formattedMinute:00")
            }, hour, minute, true)
            timePicker.show()
        }

        btnSubmit.setOnClickListener {
            val vehicle = etVehicle.text.toString().trim()
            val date = etDate.text.toString().trim()
            val time = etTime.text.toString().trim()
            val serviceType = spinnerService.selectedItem.toString()

            if (vehicle.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            submitBooking(vehicle, date, time, serviceType)
        }
    }

    private fun submitBooking(vehicle: String, date: String, time: String, service: String) {
        progressBar.visibility = View.VISIBLE
        btnSubmit.text = ""

        // Get the token we saved during login
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val request = BookingRequest(service, vehicle, date, time)

                // Pass the Bearer token in the header
                val response = ApiClient.getApi().createBooking("Bearer $token", request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@BookingActivity, "Booking Confirmed!", Toast.LENGTH_LONG).show()
                    finish() // Go back to the dashboard
                } else {
                    Toast.makeText(this@BookingActivity, "Failed: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookingActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                btnSubmit.text = "Confirm Booking"
            }
        }
    }
}