package ru.maproject.qrcode.data.qrCode

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.POST
import retrofit2.http.Query

data class QrCode(
    val url: String
)

interface QrCodeApi {

    @POST("/macros/s/AKfycbzIY2OqtEPSZAsY6j2eBza3LOZpZ_WlRJOwO1CVt9mKqFgkGcZSsYNMLzhoLw8UPj0xvg/exec")
    suspend fun get(
        @Query("number") number: String
    ): QrCode
}

val gson: Gson by lazy {
    GsonBuilder()
        .setLenient()
        .create()
}

val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://script.google.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

val qrCodeApi by lazy {
    retrofit.create<QrCodeApi>()
}