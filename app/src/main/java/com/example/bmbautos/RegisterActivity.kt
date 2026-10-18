package com.example.bmbautos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bmbautos.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    // 1. Setup ViewBinding
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. Initialize Binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Setup Button Click Listeners
        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.tvBackToLogin.setOnClickListener {
            // Go back to the previous screen (Login)
            finish()
        }
    }

    private fun registerUser() {
        // Get the text from the input fields
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Basic Validation: Check if fields are empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading spinner and disable button to prevent double-clicks
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false

        // 4. Make the API Call using Coroutines
        lifecycleScope.launch {
            try {
                // FIXED: Using ApiClient.getApi() instead of getApiService()
                val apiService = ApiClient.getApi()

                // Call the register endpoint from your ApiService.kt
                val response = apiService.register(RegisterRequest(name, email, password))

                // Hide loading spinner and re-enable button
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true

                // 5. Handle the Response
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@RegisterActivity, "Registration Successful! Please log in.", Toast.LENGTH_LONG).show()

                    // Navigate back to LoginActivity
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    // Clear activity stack so they can't press 'back' to get to register
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    // Show error message from API (e.g., "Email already exists")
                    val errorMsg = response.body()?.message ?: "Registration failed. Please try again."
                    Toast.makeText(this@RegisterActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Handle network errors (e.g., No internet, server down)
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
                Toast.makeText(this@RegisterActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}