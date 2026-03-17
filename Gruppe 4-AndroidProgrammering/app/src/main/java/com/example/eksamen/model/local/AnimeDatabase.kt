package com.example.eksamen.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eksamen.model.Anime

//Room-databasen for hele appen
@Database(entities = [
    Anime::class,
    FavoritesAnime::class,
    AnimeIdea::class
                     ],
    version = 9,
    exportSchema = false
)
abstract class AnimeDatabase : RoomDatabase(){

    //Dao for favoritter og lagret aniem fra API
    abstract fun animeDao(): AnimeDao

    //Dao fro brukerskapte anime ideer
    abstract fun animeIdeasDao(): AnimeIdeasDao
}

