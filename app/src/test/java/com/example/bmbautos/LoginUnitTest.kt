package com.example.bmbautos

import org.junit.Assert.assertEquals
import org.junit.Test

class LoginUnitTest {

    @Test
    fun loginRequest_containsCorrectData() {
        // 1. Arrange: Create a test login request
        val email = "thabo@bmbautos.co.za"
        val password = "welcome123"

        // 2. Act: Pass it to the model
        val request = LoginRequest(email, password)

        // 3. Assert: Check if the data is correct
        assertEquals("thabo@bmbautos.co.za", request.email)
        assertEquals("welcome123", request.password)
    }

    @Test
    fun tradeInRequest_containsCorrectData() {
        val request = TradeInRequest(
            make = "Toyota",
            model = "Hilux",
            year = 2022,
            price = "R 500,000",
            type = "Bakkie"
        )

        assertEquals("Toyota", request.make)
        assertEquals(2022, request.year)
    }
}