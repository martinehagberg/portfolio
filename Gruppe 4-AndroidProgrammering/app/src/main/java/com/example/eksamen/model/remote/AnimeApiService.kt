package com.example.eksamen.model.remote

import com.example.eksamen.model.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeApiService {

    // GET-anrop til https://api.jikan.moe/v4/anime
    @GET("anime")
    suspend fun getAnimeList(
        @Query("page") page: Int = 1
    ): AnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") animeId: Int
    ): AnimeByIdResponse
}