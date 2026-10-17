package com.example.bmbautos

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CarsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchCars()
    }

    private fun fetchCars() {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi().getCars()

                if (response.isSuccessful && response.body()?.status == "success") {
                    val cars = response.body()?.data ?: emptyList()

                    if (cars.isEmpty()) {
                        Toast.makeText(this@CarsActivity, "No cars available", Toast.LENGTH_SHORT).show()
                    } else {
                        recyclerView.adapter = CarAdapter(cars)
                    }
                } else {
                    Toast.makeText(this@CarsActivity, "Failed to load cars", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CarsActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}