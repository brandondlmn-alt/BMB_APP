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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoogleSSO: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvServerSettings: TextView

    // Google SSO variables
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ApiClient.init(this)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoogleSSO = findViewById(R.id.btnGoogleSSO)
        progressBar = findViewById(R.id.progressBar)
        tvServerSettings = findViewById(R.id.tvServerSettings)

        updateServerText()
        tvServerSettings.setOnClickListener { showServerDialog() }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("84226084624-ch3mc9e66m4751drht9stmd2aaic11vv.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            performLogin(email, password)
        }

        btnGoogleSSO.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    // Handle Google Sign-In Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken)
                } else {
                    Toast.makeText(this, "Google ID Token is null", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Verify token with Firebase
    private fun firebaseAuthWithGoogle(idToken: String) {
        progressBar.visibility = android.view.View.VISIBLE
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        sendGoogleDataToBackend(user.email ?: "", user.displayName ?: "Google User", user.uid)
                    }
                } else {
                    progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, "Firebase Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Send data to your PHP API
    private fun sendGoogleDataToBackend(email: String, name: String, googleId: String) {
        lifecycleScope.launch {
            try {
                val requestBody = mapOf("email" to email, "name" to name, "google_id" to googleId)
                val response = ApiClient.getApi().ssoLogin(requestBody)

                if (response.isSuccessful && response.body()?.status == "success") {
                    val loginResponse = response.body()
                    saveUserAndNavigate(loginResponse?.token, loginResponse?.user)
                } else {
                    Toast.makeText(this@LoginActivity, "Backend SSO Failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        progressBar.visibility = android.view.View.VISIBLE
        btnLogin.text = ""

        lifecycleScope.launch {
            try {
                val response = ApiClient.getApi().login(LoginRequest(email, password))
                if (response.isSuccessful && response.body()?.status == "success") {
                    val loginResponse = response.body()
                    saveUserAndNavigate(loginResponse?.token, loginResponse?.user)
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

    private fun saveUserAndNavigate(token: String?, user: User?) {
        Toast.makeText(this@LoginActivity, "Welcome, ${user?.name}!", Toast.LENGTH_SHORT).show()
        val sharedPref = getSharedPreferences("BMB_PREFS", MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("API_TOKEN", token)
            putString("USER_NAME", user?.name)
            putString("USER_ROLE", user?.role)
            apply()
        }
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun updateServerText() {
        tvServerSettings.text = "Current Server:\n${ApiClient.currentBaseUrl}\n(Tap to change)"
    }

    // --- UPDATED SERVER DIALOG FOR NGROK ---
    private fun showServerDialog() {
        val servers = arrayOf(
            "VM Emulator (10.0.2.2)",
            "Ngrok Tunnel (Physical Phone)",
            "Native PC (192.168.0.188)"
        )

        AlertDialog.Builder(this)
            .setTitle("Select API Server")
            .setItems(servers) { _, which ->
                val newUrl = when (which) {
                    0 -> "http://10.0.2.2/BMB_AUTOS/api/"
                    1 -> "https://abstain-sixfold-nervy.ngrok-free.dev/BMB_AUTOS/api/"
                    2 -> "http://192.168.0.188/BMB_AUTOS/api/"
                    else -> "http://10.0.2.2/BMB_AUTOS/api/"
                }

                ApiClient.changeUrl(this, newUrl)
                updateServerText()
                Toast.makeText(this, "Server switched!", Toast.LENGTH_SHORT).show()
            }.show()
    }
}