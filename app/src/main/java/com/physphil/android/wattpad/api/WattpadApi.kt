package com.physphil.android.wattpad.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://www.wattpad.com/api/v3/"

/**
 * Singleton to access the Wattpad API
 * Copyright (c) 2017 Phil Shadlyn
 */
object WattpadApi {

    val service: WattpadService by lazy {
        // Lazily initialize the Wattpad Retrofit Service
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WattpadService::class.java)
    }
}