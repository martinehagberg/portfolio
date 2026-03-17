package com.example.eksamen.model

import com.example.eksamen.model.remote.AnimeApiModel

//Representerer den øverste respons-strukturen fra APIer når vi henter en liste med anime.
data class AnimeResponse (
    val data: List<AnimeApiModel>
)