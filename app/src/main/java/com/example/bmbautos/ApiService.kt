package com.example.bmbautos

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// --- Login Models ---
data class LoginRequest(val email: String, val password: String)
data class User(val id: Int, val name: String, val email: String, val role: String)
data class LoginResponse(val status: String, val message: String, val token: String?, val user: User?)

// --- Car Models ---
data class Car(
    val id: Int,
    val make: String,
    val model: String,
    val year: Int,
    val price: String,
    val mileage: String,
    val image: String,
    val status: String
)

data class CarsResponse(
    val status: String,
    val count: Int,
    val data: List<Car>
)

// --- API Interface ---
interface ApiService {
    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("cars.php")
    suspend fun getCars(): Response<CarsResponse>
}