package com.example.bmbautos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.bmbautos.databinding.ActivitySettingsBinding
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("BMB_PREFS", Context.MODE_PRIVATE)

        // Load current user data
        val name = sharedPref.getString("USER_NAME", "User")
        val email = sharedPref.getString("USER_EMAIL", "user@example.com")
        
        binding.etName.setText(name)
        binding.etEmail.setText(email)
        binding.tvProfileName.text = name
        binding.tvProfileEmail.text = email

        // Dark Mode Logic
        val isDarkMode = sharedPref.getBoolean("DARK_MODE", false)
        binding.switchDarkMode.isChecked = isDarkMode
        
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("DARK_MODE", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Notification Toggles (Mock persistence)
        binding.switchEmail.isChecked = sharedPref.getBoolean("NOTIFY_EMAIL", true)
        binding.switchPush.isChecked = sharedPref.getBoolean("NOTIFY_PUSH", true)

        binding.btnSave.setOnClickListener {
            saveProfile()
        }

        binding.btnLogout.setOnClickListener {
            sharedPref.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.isEnabled = false

        val sharedPref = getSharedPreferences("BMB_PREFS", Context.MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val notifyEmail = if (binding.switchEmail.isChecked) 1 else 0
                val notifyPush = if (binding.switchPush.isChecked) 1 else 0
                
                val request = UpdateProfileRequest(name, email, notifyEmail, notifyPush)
                val response = ApiClient.getApi().updateProfile("Bearer $token", request)

                binding.progressBar.visibility = View.GONE
                binding.btnSave.isEnabled = true

                if (response.isSuccessful) {
                    sharedPref.edit().apply {
                        putString("USER_NAME", name)
                        putString("USER_EMAIL", email)
                        putBoolean("NOTIFY_EMAIL", binding.switchEmail.isChecked)
                        putBoolean("NOTIFY_PUSH", binding.switchPush.isChecked)
                        apply()
                    }
                    binding.tvProfileName.text = name
                    binding.tvProfileEmail.text = email
                    Toast.makeText(this@SettingsActivity, "Profile Updated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SettingsActivity, "Update failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.btnSave.isEnabled = true
                Toast.makeText(this@SettingsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
