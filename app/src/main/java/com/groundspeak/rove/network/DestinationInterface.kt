package com.groundspeak.rove.network

import com.groundspeak.rove.models.Destination
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DestinationInterface {

    @GET("v3/9326cd89-1243-470b-b435-cd7ebd9ad5ba")
    fun getDestinations(): Call<List<Destination>>

    companion object {
        var BASE_URL = "https://run.mocky.io/"

        fun create(): DestinationInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(DestinationInterface::class.java)
        }
    }
}