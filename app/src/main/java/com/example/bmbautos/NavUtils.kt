package com.example.bmbautos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

object NavUtils {
    fun setupUniversalNavbar(activity: AppCompatActivity, selectedItemId: Int) {
        val bottomNav = activity.findViewById<BottomNavigationView>(R.id.bottomNav) ?: return
        bottomNav.selectedItemId = selectedItemId
        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == selectedItemId) return@setOnItemSelectedListener true
            when (item.itemId) {
                R.id.nav_home -> {
                    if (activity !is MainActivity) {
                        activity.startActivity(Intent(activity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        activity.finish()
                    }
                    true
                }
                R.id.nav_search -> {
                    if (activity !is CarsActivity) {
                        activity.startActivity(Intent(activity, CarsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        activity.finish()
                    }
                    true
                }
                R.id.nav_garage -> {
                    if (activity !is MyGarageActivity) {
                        activity.startActivity(Intent(activity, MyGarageActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        activity.finish()
                    }
                    true
                }
                R.id.nav_bookings -> {
                    if (activity !is BookingsActivity) {
                        activity.startActivity(Intent(activity, BookingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        activity.finish()
                    }
                    true
                }
                R.id.nav_profile -> {
                    if (activity !is SettingsActivity) {
                        activity.startActivity(Intent(activity, SettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        activity.finish()
                    }
                    true
                }
                else -> false
            }
        }
    }
}
