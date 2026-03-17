package com.example.eksamen.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.eksamen.model.Anime
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//Dao for all databaseoperasjoner
@Dao
interface AnimeDao {

    //Setter inn en liste med anime-objekter i databasen
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: List<Anime>)

    //Returnerer alle anme i databasen
    @Query("SELECT * FROM Anime")
    suspend fun getAllAnime(): List<Anime>

    //Henter en anime basert på ID
    @Query("SELECT * FROM Anime WHERE id = :id LIMIT 1")
    suspend fun getAnimeById(id: Int): Anime?

    //Henter alle anime brukeren har markert som favoritter
    @Query("SELECT * FROM favorite_anime")
    fun getAllFavorites(): Flow<List<FavoritesAnime>>

    //legger til eller oppdaterer en favoritt i databasen
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(anime: FavoritesAnime)

    //Sletter en anime fra favoritter basert på ID
    @Query("DELETE FROM favorite_anime WHERE id = :id")
    suspend fun removeFavorite(id: Int)
}
