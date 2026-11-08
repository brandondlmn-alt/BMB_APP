package com.example.bmbautos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.materialswitch.MaterialSwitch
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var switchEmail: MaterialSwitch
    private lateinit var switchPush: MaterialSwitch
    private lateinit var switchDarkMode: MaterialSwitch
    private lateinit var btnEditProfile: TextView
    private lateinit var btnChangePassword: TextView
    private lateinit var btnHelp: TextView
    private lateinit var btnTerms: TextView
    private lateinit var btnLogout: com.google.android.material.button.MaterialButton
    private lateinit var btnDeleteAccount: com.google.android.material.button.MaterialButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tvProfileName = findViewById(R.id.tvProfileName)
        tvProfileEmail = findViewById(R.id.tvProfileEmail)
        switchEmail = findViewById(R.id.switchEmail)
        switchPush = findViewById(R.id.switchPush)
        switchDarkMode = findViewById(R.id.switchDarkMode)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnHelp = findViewById(R.id.btnHelp)
        btnTerms = findViewById(R.id.btnTerms)
        btnLogout = findViewById(R.id.btnLogout)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)
        progressBar = findViewById(R.id.progressBar)

        NavUtils.setupUniversalNavbar(this, R.id.nav_profile)

        // Load current user data
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val name = sharedPref.getString("USER_NAME", "User Name")
        val email = sharedPref.getString("USER_EMAIL", "user@example.com")
        tvProfileName.text = name
        tvProfileEmail.text = email

        switchEmail.isChecked = true
        switchPush.isChecked = true

        // Dark Mode Toggle Logic
        val isDarkMode = sharedPref.getBoolean("DARK_MODE", false)
        switchDarkMode.isChecked = isDarkMode
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("DARK_MODE", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        btnHelp.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Help & FAQ")
                .setMessage("For assistance, contact support@bmbautos.com or call our 24/7 hotline at 0800-BMB-AUTOS.")
                .setPositiveButton("OK", null)
                .show()
        }

        btnTerms.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Terms & Conditions")
                .setMessage("By using BMB Autos, you agree to our vehicle inspection terms, financing guidelines, and privacy policy.")
                .setPositiveButton("OK", null)
                .show()
        }

        btnLogout.setOnClickListener {
            sharedPref.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnDeleteAccount.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action is irreversible.")
                .setPositiveButton("Delete") { _, _ ->
                    sharedPref.edit().clear().apply()
                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showEditProfileDialog() {
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val currentName = sharedPref.getString("USER_NAME", "") ?: ""
        val currentEmail = sharedPref.getString("USER_EMAIL", "") ?: ""

        val context = this
        val layout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(48, 32, 48, 16)
        }

        val etName = android.widget.EditText(context).apply {
            hint = "Full Name"
            setText(currentName)
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        }

        val etEmail = android.widget.EditText(context).apply {
            hint = "Email Address"
            setText(currentEmail)
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 24 }
        }

        layout.addView(etName)
        layout.addView(etEmail)

        AlertDialog.Builder(context)
            .setTitle("Edit Profile Details")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val newName = etName.text.toString().trim()
                val newEmail = etEmail.text.toString().trim()
                if (newName.isEmpty() || newEmail.isEmpty()) {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                updateProfileApi(newName, newEmail)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showChangePasswordDialog() {
        val context = this
        val layout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(48, 32, 48, 16)
        }

        val etCurrentPass = android.widget.EditText(context).apply {
            hint = "Current Password"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val etNewPass = android.widget.EditText(context).apply {
            hint = "New Password"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 24 }
        }

        layout.addView(etCurrentPass)
        layout.addView(etNewPass)

        AlertDialog.Builder(context)
            .setTitle("Change Password")
            .setView(layout)
            .setPositiveButton("Update") { _, _ ->
                val currentPass = etCurrentPass.text.toString().trim()
                val newPass = etNewPass.text.toString().trim()
                if (currentPass.isEmpty() || newPass.isEmpty()) {
                    Toast.makeText(context, "Passwords cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                changePasswordApi(currentPass, newPass)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateProfileApi(name: String, email: String) {
        progressBar.visibility = View.VISIBLE
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val request = UpdateProfileRequest(name, email, 1, 1)
                val response = ApiClient.getApi().updateProfile("Bearer $token", request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    sharedPref.edit().apply {
                        putString("USER_NAME", name)
                        putString("USER_EMAIL", email)
                        apply()
                    }
                    tvProfileName.text = name
                    tvProfileEmail.text = email
                    Toast.makeText(this@SettingsActivity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    NotificationHelper.showSuccessDialog(this@SettingsActivity, "Profile Updated", "Your details have been saved.")
                } else {
                    Toast.makeText(this@SettingsActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun changePasswordApi(currentPass: String, newPass: String) {
        progressBar.visibility = View.VISIBLE
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("API_TOKEN", "") ?: ""

        lifecycleScope.launch {
            try {
                val request = ChangePasswordRequest(currentPass, newPass)
                val response = ApiClient.getApi().changePassword("Bearer $token", request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@SettingsActivity, "Password changed successfully!", Toast.LENGTH_SHORT).show()
                    NotificationHelper.showSuccessDialog(this@SettingsActivity, "Password Updated", "Your password has been changed.")
                } else {
                    val msg = response.body()?.message ?: "Failed to change password"
                    Toast.makeText(this@SettingsActivity, msg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
