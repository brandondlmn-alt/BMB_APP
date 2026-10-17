package com.example.bmbautos
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// --- Request Models ---
data class LoginRequest(
    val email: String,
    val password: String
)

// --- Response Models ---
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String?,
    val user: User?
)

// --- API Interface ---
interface ApiService {
    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}