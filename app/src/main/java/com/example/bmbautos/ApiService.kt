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
data class Booking(val id: Int, val vehicle_info: String, val service_type: String, val booking_date: String, val booking_time: String, val status: String)
data class BookingsResponse(val status: String, val data: List<Booking>)

// --- Trade-In Models ---
data class TradeInRequest(
    val make: String,
    val model: String,
    val year: Int,
    val price: String,
    val type: String
)

//Settings & Profile
data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val notify_email: Int,
    val notify_push: Int
)

// --- Generic Response (used for Bookings and Trade-Ins) ---
data class GenericResponse(val status: String, val message: String)

// --- API Interface ---
interface ApiService {

    // 1. Standard Login
    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // 2. Google SSO Login
    @POST("sso_login.php")
    suspend fun ssoLogin(@Body request: Map<String, String>): Response<LoginResponse>

    // 3. Fetch Showroom Cars
    @GET("cars.php")
    suspend fun getCars(): Response<CarsResponse>

    // 4. Create a Booking
    @POST("bookings.php")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body request: BookingRequest
    ): Response<GenericResponse>

    // 5. Fetch User's Bookings
    @GET("bookings.php")
    suspend fun getMyBookings(
        @Header("Authorization") token: String
    ): Response<BookingsResponse>

    // 6. Create a Trade-In Request
    @POST("trade_in.php")
    suspend fun createTradeIn(
        @Header("Authorization") token: String,
        @Body request: TradeInRequest
    ): Response<GenericResponse>

    //Settings & Profile
    @POST("update_profile.php")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<GenericResponse>
}