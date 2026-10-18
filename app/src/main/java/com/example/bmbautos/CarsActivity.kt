package com.example.bmbautos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CarsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var etSearch: EditText
    private lateinit var carAdapter: CarAdapter
    private var allCars: List<Car> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        etSearch = findViewById(R.id.etSearch)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with the Buy Click listener
        carAdapter = CarAdapter(emptyList()) { car ->
            showBuyConfirmation(car)
        }
        recyclerView.adapter = carAdapter

        fetchCars()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCars(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showBuyConfirmation(car: Car) {
        AlertDialog.Builder(this)
            .setTitle("Purchase Vehicle")
            .setMessage("Do you want to buy the ${car.make} ${car.model} for ${car.price}?")
            .setPositiveButton("Confirm") { _, _ ->
                executePurchase(car)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun executePurchase(car: Car) {
        progressBar.visibility = View.VISIBLE

        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi().purchaseCar("Bearer $token", PurchaseRequest(car.id, car.price))

                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@CarsActivity, "Purchase Successful!", Toast.LENGTH_LONG).show()
                    fetchCars() // Reload the list to show it is Sold
                } else {
                    // THIS WILL SHOW THE EXACT DATABASE ERROR
                    Toast.makeText(this@CarsActivity, "Failed: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CarsActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun filterCars(query: String) {
        val filteredList = allCars.filter {
            it.make.lowercase().contains(query.lowercase()) ||
                    it.model.lowercase().contains(query.lowercase())
        }
        carAdapter.updateList(filteredList)
    }

    private fun fetchCars() {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi().getCars()
                if (response.isSuccessful && response.body()?.status == "success") {
                    allCars = response.body()?.data ?: emptyList()
                    carAdapter.updateList(allCars)
                }
            } catch (e: Exception) {
                Toast.makeText(this@CarsActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}