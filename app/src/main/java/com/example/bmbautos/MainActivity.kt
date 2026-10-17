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

        // 1. Welcome the User (Fetch from SharedPreferences)
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "User")
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text = "Welcome back,\n$userName"

        // 2. Handle Quick Action Clicks
        findViewById<MaterialCardView>(R.id.cardBrowse).setOnClickListener {
            // Navigate to the Showroom (CarsActivity)
            val intent = Intent(this, CarsActivity::class.java)
            startActivity(intent)
        }

        findViewById<MaterialCardView>(R.id.cardService).setOnClickListener {
            // Navigate to the Service Booking Form (BookingActivity)
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        findViewById<MaterialCardView>(R.id.cardTradeIn).setOnClickListener {
            Toast.makeText(this, "Opening Trade-In Form...", Toast.LENGTH_SHORT).show()
            // Later: startActivity(Intent(this, TradeInActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardHistory).setOnClickListener {
            Toast.makeText(this, "Opening History...", Toast.LENGTH_SHORT).show()
        }

        // 3. Handle Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_search -> {
                    Toast.makeText(this, "Search coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_bookings -> {
                    Toast.makeText(this, "Bookings coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    // For now, the profile button acts as a Logout button
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun logout() {
        // Clear the token and user data so they have to log in again
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Go back to Login screen and close the dashboard
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}