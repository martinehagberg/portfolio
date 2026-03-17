package com.example.eksamen.model.remote


import com.google.gson.annotations.SerializedName

data class AnimeApiModel (
    @SerializedName("mal_id")
    val id: Int,
    val title: String,
    val synopsis: String?,
    val images: AnimeImages,
    val score: Double?,
    val status: String?,
    val episodes: Int?,
    val genres: List<Genre>?
)

data class AnimeImages(
    val jpg: AnimeImagesUrl
)
data class AnimeImagesUrl(
    @SerializedName("image_url")
    val imageUrl: String
)

data class Genre(
    val name: String
)