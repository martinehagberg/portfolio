package com.example.eksamen.repository

import android.content.Context
import androidx.room.Room
import com.example.eksamen.model.Anime
import com.example.eksamen.model.local.AnimeDatabase
import com.example.eksamen.model.remote.RetrofitInstance


object AnimeRepository {
    private lateinit var db: AnimeDatabase
    val animeDao by lazy { db.animeDao() }
    val animeIdeasDao by lazy {db.animeIdeasDao()}

    fun initialize(context: Context) {
        db = Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            "anime-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
        fun getDatabase(): AnimeDatabase {
            return db
        }


        suspend fun getAnime(): List<Anime> {
            return try {
                val response = RetrofitInstance.api.getAnimeList()
                val mapped = response.data.map { api ->
                    Anime(
                        id = api.id,
                        title = api.title,
                        synopsis = api.synopsis ?: "",
                        imageUrl = api.images.jpg.imageUrl,
                        score = api.score,
                        episodes = api.episodes,
                        genres = api.genres?.joinToString (", "){genre -> genre.name} ?: "Ingen sjanger"
                    )
                }

                animeDao.insertAnime(mapped)
                animeDao.getAllAnime()

            } catch (e: Exception) {
                animeDao.getAllAnime()
            }
        }

        suspend fun getAnimeById(id: Int): Anime? {
            return try {
                val response = RetrofitInstance.api.getAnimeById(id)
                val api = response.data
                Anime(
                    id = api.id,
                    title = api.title,
                    synopsis = api.synopsis ?: "",
                    imageUrl = api.images.jpg.imageUrl,
                    score = api.score,
                    episodes = api.episodes,
                    genres = api.genres?.joinToString (", "){genre -> genre.name} ?: "Ingen sjanger"
                )
            } catch (e: Exception) {
                null
            }
        }

    }
