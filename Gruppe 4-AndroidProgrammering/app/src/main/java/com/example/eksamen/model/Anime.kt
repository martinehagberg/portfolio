package com.example.eksamen.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//Representerer en Anime-post i databasen og i UI
//brukes som room-entitet for lokal lagring og som domenemodell
@Entity
data class Anime (
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String,
    val score: Double?,
    val episodes: Int?,
    val genres: String?,
    val synopsis: String?
)

