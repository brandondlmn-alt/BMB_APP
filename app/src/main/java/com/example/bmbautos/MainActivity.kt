package com.example.bmbautos

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "User")
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text = "Welcome back,\n$userName"

        // 1. Browse Cars
        findViewById<MaterialCardView>(R.id.cardBrowse).setOnClickListener {
            startActivity(Intent(this, CarsActivity::class.java))
        }

        // 2. Service Booking
        findViewById<MaterialCardView>(R.id.cardService).setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        // 3. Trade-In
        findViewById<MaterialCardView>(R.id.cardTradeIn).setOnClickListener {
            findViewById<MaterialCardView>(R.id.cardTradeIn).setOnClickListener {
                startActivity(Intent(this, TradeInActivity::class.java))
            }
        }

        // 4. View History
        findViewById<MaterialCardView>(R.id.cardHistory).setOnClickListener {
            startActivity(Intent(this, BookingsActivity::class.java))
        }

        // Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_search -> {
                    Toast.makeText(this, "Search coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_bookings -> {
                    startActivity(Intent(this, BookingsActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}