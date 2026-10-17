package com.example.bmbautos
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val PREFS_NAME = "BMB_PREFS"
    private const val KEY_BASE_URL = "BASE_URL"

    // Default to Emulator URL
    private const val DEFAULT_URL = "http://10.0.2.2/BMB_AUTOS/api/"

    var currentBaseUrl: String = DEFAULT_URL
        private set

    private var retrofit: Retrofit? = null

    fun init(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        currentBaseUrl = sharedPref.getString(KEY_BASE_URL, DEFAULT_URL) ?: DEFAULT_URL
        buildRetrofit()
    }

    fun changeUrl(context: Context, newUrl: String) {
        currentBaseUrl = newUrl
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().putString(KEY_BASE_URL, newUrl).apply()
        buildRetrofit()
    }

    private fun buildRetrofit() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(currentBaseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApi(): ApiService {
        if (retrofit == null) {
            buildRetrofit()
        }
        return retrofit!!.create(ApiService::class.java)
    }
}