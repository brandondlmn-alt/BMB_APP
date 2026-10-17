package com.example.bmbautos

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class BookingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchBookings()
    }

    private fun fetchBookings() {
        progressBar.visibility = View.VISIBLE
        tvEmpty.visibility = View.GONE

        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi().getMyBookings("Bearer $token")

                if (response.isSuccessful && response.body()?.status == "success") {
                    val bookings = response.body()?.data ?: emptyList()

                    if (bookings.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                    } else {
                        recyclerView.adapter = BookingAdapter(bookings)
                    }
                } else {
                    Toast.makeText(this@BookingsActivity, "Failed to load bookings", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookingsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}