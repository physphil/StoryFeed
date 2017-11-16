package com.physphil.android.wattpad.api

import com.physphil.android.wattpad.api.model.StoriesApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service to define methods to consume Wattpad API
 * Copyright (c) 2017 Phil Shadlyn
 */
interface WattpadService {
    /**
     * Get list of stories from API
     * @param offset the offset used by the API to shift the returned results, defaults to 0
     * @param limit the maximum number of stories to return, defaults to 30
     * @return an Observable which will emit callback events to a subscriber
     */
    @GET("stories?fields=stories(id,title,cover,user)")
    fun getStories(@Query("offset") offset: Int = 0, @Query("limit") limit: Int = 30): Observable<StoriesApiResponse>
}
