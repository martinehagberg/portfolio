package com.example.eksamen.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//Lokal database-modell for en anime som brukeren har markert som favoritt.
@Entity(tableName = "favorite_anime")
data class FavoritesAnime (
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String?,
    val timestamp: Long = System.currentTimeMillis()
)