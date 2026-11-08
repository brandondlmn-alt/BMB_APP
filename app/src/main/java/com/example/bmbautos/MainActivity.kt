package com.example.bmbautos

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request POST_NOTIFICATIONS permission for Android 13+ (API 33+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

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

        NavUtils.setupUniversalNavbar(this, R.id.nav_home)
    }
}
