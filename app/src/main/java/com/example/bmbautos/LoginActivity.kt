package com.example.bmbautos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvServerSettings: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize ApiClient with saved URL preferences
        ApiClient.init(this)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        tvServerSettings = findViewById(R.id.tvServerSettings)

        // Update the text to show current URL
        updateServerText()

        // Developer feature: Tap to change server
        tvServerSettings.setOnClickListener {
            showServerDialog()
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(email, password)
        }
    }

    private fun updateServerText() {
        tvServerSettings.text = "Current Server:\n${ApiClient.currentBaseUrl}\n(Tap to change)"
    }

    private fun showServerDialog() {
        val servers = arrayOf(
            "VM Emulator (10.0.2.2)",
            "Native PC (192.168.1.15)" // Change this to your actual PC IP for later
        )

        AlertDialog.Builder(this)
            .setTitle("Select API Server")
            .setItems(servers) { _, which ->
                val newUrl = when (which) {
                    0 -> "http://10.0.2.2/BMB_AUTOS/api/"
                    1 -> "http://192.168.0.188/BMB_AUTOS/api/"
                    else -> "http://10.0.2.2/BMB_AUTOS/api/"
                }

                ApiClient.changeUrl(this, newUrl)
                updateServerText()
                Toast.makeText(this, "Server switched!", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun performLogin(email: String, password: String) {
        progressBar.visibility = android.view.View.VISIBLE
        btnLogin.text = ""

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi().login(LoginRequest(email, password))

                if (response.isSuccessful && response.body()?.status == "success") {
                    val loginResponse = response.body()
                    val token = loginResponse?.token
                    val user = loginResponse?.user

                    Toast.makeText(this@LoginActivity, "Welcome, ${user?.name}!", Toast.LENGTH_SHORT).show()

                    // Save Token and User Info
                    val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
                    sharedPref.edit().apply {
                        putString("API_TOKEN", token)
                        putString("USER_NAME", user?.name)
                        putString("USER_ROLE", user?.role)
                        apply()
                    }

                    // Navigate to Dashboard
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
                btnLogin.text = "Sign In"
            }
        }
    }
}