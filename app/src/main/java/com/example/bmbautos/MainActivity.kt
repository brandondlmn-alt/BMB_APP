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

        findViewById<MaterialCardView>(R.id.cardBrowse).setOnClickListener {
            startActivity(Intent(this, CarsActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardService).setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        // FIXED TRADE-IN BUG
        findViewById<MaterialCardView>(R.id.cardTradeIn).setOnClickListener {
            startActivity(Intent(this, TradeInActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardHistory).setOnClickListener {
            startActivity(Intent(this, BookingsActivity::class.java))
        }

        // NEW PURCHASES BUTTON
        findViewById<MaterialCardView>(R.id.cardPurchases).setOnClickListener {
            startActivity(Intent(this, PurchasesActivity::class.java))
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_search -> {
                    startActivity(Intent(this, CarsActivity::class.java))
                    true
                }
                R.id.nav_bookings -> {
                    startActivity(Intent(this, BookingsActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}