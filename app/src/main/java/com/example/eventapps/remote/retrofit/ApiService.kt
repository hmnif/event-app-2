package com.example.eventapps.remote.retrofit

import com.example.eventapps.remote.response.DetailEventResponse
import com.example.eventapps.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
    ): Call<DetailEventResponse>

    // upcoming events
    @GET("events")
    suspend fun getUpcomingEvents(
        @Query("active") active: Int = 1
    ): EventResponse

    // upcoming 5 events
    @GET("events")
    suspend fun getFiveUpcomingEvents(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 5
    ): EventResponse

    // finished events
    @GET("events")
    suspend fun getFinishedEvents(
        @Query("active") active: Int = 0
    ): EventResponse

    // finished 5 events
    @GET("events")
    suspend fun getFiveFinishedEvents(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int = 5
    ): EventResponse

    // search finished and upcoming event
    @GET("events")
    suspend fun getSearchedEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String = "devcoach"
    ): EventResponse

    @GET("events")
    fun getNearbyEvent (
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 1
    ): Call<EventResponse>

}