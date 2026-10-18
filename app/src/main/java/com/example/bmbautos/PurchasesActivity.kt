package com.example.bmbautos

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class PurchasesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchases)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val tvEmpty = findViewById<TextView>(R.id.tvEmpty)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi().getMyPurchases("Bearer $token")
                if (response.isSuccessful && response.body()?.status == "success") {
                    val purchases = response.body()?.data ?: emptyList()
                    if (purchases.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                    } else {
                        recyclerView.adapter = PurchasesAdapter(purchases)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@PurchasesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}