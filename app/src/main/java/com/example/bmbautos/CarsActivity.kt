package com.example.bmbautos

import android.content.Intent
import android.net.Uri
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
        NavUtils.setupUniversalNavbar(this, R.id.nav_search)

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
            .setMessage("Do you want to buy the ${car.color} ${car.year} ${car.make} ${car.model} for ${car.price}?")
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
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    val transactionId = body.transaction_id ?: 0
                    val receiptUrl = body.receipt_url ?: "receipt.php?transaction_id=$transactionId"
                    val fullReceiptUrl = "http://ec2-16-171-44-236.eu-north-1.compute.amazonaws.com/$receiptUrl"

                    // SHOW THE NOTIFICATIONS
                    NotificationHelper.showSystemNotification(this@CarsActivity, "Purchase Complete", "Congratulations on your new ${car.make} ${car.model}!")

                    // Show Success Dialog with View Receipt Button
                    AlertDialog.Builder(this@CarsActivity)
                        .setTitle("Purchase Successful!")
                        .setMessage("You are now the proud owner of a ${car.make} ${car.model}.\nTransaction ID: #$transactionId")
                        .setPositiveButton("View Receipt") { _, _ ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullReceiptUrl))
                            startActivity(intent)
                        }
                        .setNegativeButton("Close", null)
                        .show()

                    fetchCars() // Reload the list to show updated stock quantity & status
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = if (errorBody.isNullOrEmpty()) body?.message ?: "Unknown Error (Code: ${response.code()})" else errorBody
                    Toast.makeText(this@CarsActivity, "Failed: $errorMsg", Toast.LENGTH_LONG).show()
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
                    it.model.lowercase().contains(query.lowercase()) ||
                    it.color.lowercase().contains(query.lowercase())
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
