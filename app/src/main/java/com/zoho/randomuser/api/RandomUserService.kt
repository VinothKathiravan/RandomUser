package com.zoho.randomuser.api

import com.zoho.randomuser.data.RandomUserResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserService {

    @GET("api")
    suspend fun fetchUsers(
        @Query("results") results: Int
    ): RandomUserResponse

    companion object {
        private const val BASE_URL = "https://randomuser.me/"

        fun create(): RandomUserService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RandomUserService::class.java)
        }
    }
}