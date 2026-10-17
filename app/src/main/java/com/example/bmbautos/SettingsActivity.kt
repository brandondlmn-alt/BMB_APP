package com.example.bmbautos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.materialswitch.MaterialSwitch
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var switchEmail: MaterialSwitch
    private lateinit var switchPush: MaterialSwitch
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        switchEmail = findViewById(R.id.switchEmail)
        switchPush = findViewById(R.id.switchPush)
        btnSave = findViewById(R.id.btnSave)
        btnLogout = findViewById(R.id.btnLogout)
        progressBar = findViewById(R.id.progressBar)

        // Load current user data from SharedPreferences
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        etName.setText(sharedPref.getString("USER_NAME", ""))
        etEmail.setText(sharedPref.getString("USER_EMAIL", "")) // We need to save email on login!

        // Note: For simplicity, we are defaulting the switches to true.
        // In a full app, you would fetch these from the database on screen load.
        switchEmail.isChecked = true
        switchPush.isChecked = true

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val notifyEmailInt = if (switchEmail.isChecked) 1 else 0
            val notifyPushInt = if (switchPush.isChecked) 1 else 0

            saveProfile(name, email, notifyEmailInt, notifyPushInt)
        }

        btnLogout.setOnClickListener {
            // Clear token and user data
            sharedPref.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveProfile(name: String, email: String, notifyEmail: Int, notifyPush: Int) {
        progressBar.visibility = View.VISIBLE
        btnSave.text = ""

        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val request = UpdateProfileRequest(name, email, notifyEmail, notifyPush)
                val response = ApiClient.getApi().updateProfile("Bearer $token", request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@SettingsActivity, "Settings Saved!", Toast.LENGTH_SHORT).show()

                    // Update local SharedPreferences
                    sharedPref.edit().apply {
                        putString("USER_NAME", name)
                        putString("USER_EMAIL", email)
                        apply()
                    }
                } else {
                    Toast.makeText(this@SettingsActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                btnSave.text = "Save Changes"
            }
        }
    }
}