package com.example.eksamen.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//Lokal database entitet som representerer en anime ide lagt inn av brukeren
@Entity(tableName = "anime_ideas")
data class AnimeIdea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val genre: String= "", //Tom hvis brukeren ikke valgte sjanger
    val createdAt: Long = System.currentTimeMillis()//Lagres automatisk for å støtte eventuelle tidsbaserte sortering i fremtiden
)