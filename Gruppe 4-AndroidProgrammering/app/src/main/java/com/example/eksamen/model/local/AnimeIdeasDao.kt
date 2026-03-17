package com.example.eksamen.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//Dao for tabellen som lagrer brukerens anime-ideer
@Dao
interface AnimeIdeasDao {

    //Henter alle ideer, flow for automatisk oppdatering når data endres
    @Query("SELECT * FROM anime_ideas ORDER BY id DESC")
    fun getAllIdeas(): Flow<List<AnimeIdea>>

    //Legger inn ny ide i databasen
    @Insert
    suspend fun insertIdea(idea: AnimeIdea)

    //Oppdaterer en eksisterende ide
    @Update
    suspend fun updateIdea(idea: AnimeIdea)

    //Sletter en ide fra databasen
    @Delete
    suspend fun deleteIdea(idea: AnimeIdea)

}