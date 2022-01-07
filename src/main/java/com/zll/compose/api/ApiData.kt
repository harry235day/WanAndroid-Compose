package com.zll.compose.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiData {
    private const val BASE_URL = "https://www.wanandroid.com/"

    private var retrofit: Retrofit

    init {
        retrofit = createRetrofit()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun <T> createApi(t:Class<T>): T {
       return retrofit.create(t)
    }
}