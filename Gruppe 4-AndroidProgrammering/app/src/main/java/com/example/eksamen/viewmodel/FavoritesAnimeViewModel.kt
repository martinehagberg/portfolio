package com.example.eksamen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen.model.local.AnimeDao
import com.example.eksamen.model.local.FavoritesAnime
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//ViewModel som håndterer brukerens liste av favoritt-anime
enum class SortOption(val displayName: String) { //Enum for filtrering
    NEWEST_FIRST("Nyeste først"),
    OLDEST_FIRST("Eldste først"),
    ALPHA_ASC("A-Å"),
    ALPHA_DESC("Å-A")
}

class FavoritesAnimeViewModel(private val dao: AnimeDao): ViewModel() {

    private val _sortOption = MutableStateFlow(SortOption.NEWEST_FIRST)
    val sortOption = _sortOption.asStateFlow()

    //Kombinerer DB-liste + nåværende sorteringsvalg til en strøm UI kan observere
    val favorites = combine(_sortOption, dao.getAllFavorites()) { option, list ->
        when (option) {
            SortOption.NEWEST_FIRST -> list.sortedByDescending { it.timestamp }
            SortOption.OLDEST_FIRST -> list.sortedBy { it.timestamp }
            SortOption.ALPHA_ASC -> list.sortedBy { it.title }
            SortOption.ALPHA_DESC -> list.sortedByDescending { it.title }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    //Endre sortering
    fun changeSortOption(option: SortOption) {
        _sortOption.value = option
    }

    //Legger til anime i favoritter
    fun addToFavorites(anime: FavoritesAnime){
        viewModelScope.launch {
            dao.addFavorite(anime)
        }
    }

    //Fjerner anime fra favoritter
    fun removeFromFavorites(anime: FavoritesAnime){
        viewModelScope.launch {
            dao.removeFavorite(anime.id)
        }
    }
}