package com.example.bmbautos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class BookingActivity : AppCompatActivity() {

    private lateinit var etVehicle: AutoCompleteTextView
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var spinnerService: AutoCompleteTextView
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

        // Setup Service dropdown
        val services = arrayOf(
            "Pre-Sale Evaluation", "Oil & Filter Service",
            "Brake Inspection", "Major Diagnostic", "Suspension Check"
        )
        spinnerService.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, services))

        // Fetch User's Vehicles
        fetchUserVehicles()

        // Date Picker
        etDate.setOnClickListener {
            val c = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                etDate.setText(String.format("%04d-%02d-%02d", y, m + 1, d))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        // Time Picker
        etTime.setOnClickListener {
            val c = Calendar.getInstance()
            val timePicker = TimePickerDialog(this, { _, h, m ->
                etTime.setText(String.format("%02d:%02d:00", h, m))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
            timePicker.show()
        }

        btnSubmit.setOnClickListener {
            val vehicle = etVehicle.text.toString().trim()
            val date = etDate.text.toString().trim()
            val time = etTime.text.toString().trim()
            val serviceType = spinnerService.text.toString()

            if (vehicle.isEmpty() || date.isEmpty() || time.isEmpty() || serviceType.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            submitBooking(vehicle, date, time, serviceType)
        }
    }

    private fun fetchUserVehicles() {
        progressBar.visibility = View.VISIBLE
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi().getMyCars("Bearer $token")
                if (response.isSuccessful && response.body()?.status == "success") {
                    val myCars = response.body()?.data ?: emptyList()

                    if (myCars.isEmpty()) {
                        // USER HAS NO CARS -> SHOW ERROR AND KICK THEM OUT
                        AlertDialog.Builder(this@BookingActivity)
                            .setTitle("No Vehicle Found")
                            .setMessage("You have no vehicles registered for booking. Please purchase a vehicle from our showroom or register your vehicle with us via the Trade-In feature.")
                            .setPositiveButton("OK") { _, _ ->
                                finish() // Close the booking screen
                            }
                            .setCancelable(false)
                            .show()
                    } else {
                        // USER HAS CARS -> POPULATE THE DROPDOWN
                        val carNames = myCars.map { "${it.year} ${it.make} ${it.model}" }.toTypedArray()
                        val adapter = ArrayAdapter(this@BookingActivity, android.R.layout.simple_dropdown_item_1line, carNames)
                        etVehicle.setAdapter(adapter)
                    }
                } else {
                    Toast.makeText(this@BookingActivity, "Failed to load your vehicles", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookingActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun submitBooking(vehicle: String, date: String, time: String, service: String) {
        progressBar.visibility = View.VISIBLE
        btnSubmit.text = ""

        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val request = BookingRequest(service, vehicle, date, time)
                val response = ApiClient.getApi().createBooking("Bearer $token", request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    NotificationHelper.showSuccessDialog(
                        this@BookingActivity,
                        "Booking Confirmed!",
                        "Your $service has been scheduled for $date at $time."
                    ) {
                        finish()
                    }
                    NotificationHelper.showSystemNotification(this@BookingActivity, "Service Booked", "Your appointment for $date is confirmed.")
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