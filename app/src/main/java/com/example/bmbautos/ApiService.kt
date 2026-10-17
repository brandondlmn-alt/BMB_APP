package com.example.bmbautos

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// --- Login Models ---
data class LoginRequest(val email: String, val password: String)
data class User(val id: Int, val name: String, val email: String, val role: String)
data class LoginResponse(val status: String, val message: String, val token: String?, val user: User?)

// --- Car Models ---
data class Car(val id: Int, val make: String, val model: String, val year: Int, val price: String, val mileage: String, val image: String, val status: String)
data class CarsResponse(val status: String, val count: Int, val data: List<Car>)

// --- Booking Models ---
data class BookingRequest(val service_type: String, val vehicle: String, val date: String, val time: String)
data class GenericResponse(val status: String, val message: String)

data class Booking(val id: Int, val vehicle_info: String, val service_type: String, val booking_date: String, val booking_time: String, val status: String)
data class BookingsResponse(val status: String, val data: List<Booking>)

// --- API Interface ---
interface ApiService {
    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // THIS IS THE LINE YOU WERE MISSING:
    @POST("sso_login.php")
    suspend fun ssoLogin(@Body request: Map<String, String>): Response<LoginResponse>

    @GET("cars.php")
    suspend fun getCars(): Response<CarsResponse>

    @POST("bookings.php")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body request: BookingRequest
    ): Response<GenericResponse>

    @GET("bookings.php")
    suspend fun getMyBookings(
        @Header("Authorization") token: String
    ): Response<BookingsResponse>
}