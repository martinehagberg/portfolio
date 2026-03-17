package com.example.eksamen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen.repository.AnimeRepository
import com.example.eksamen.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//ViewModel for å hente og eksponere en liste av anime objekter til UI.
class AnimeViewModel : ViewModel() {

    //Internt muterbart datasett
    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())

    //Offenltig, umuterbar StateFlow som UI observerer
    val animeList: StateFlow<List<Anime>> = _animeList

    //Auto lasting ved oppstart
    init {
        loadAnime()
    }

    //Henter anime fra repository og oppdaterer UI
    fun loadAnime() {
        viewModelScope.launch {
            try {
                val data = AnimeRepository.getAnime()
                _animeList.value = data
            }catch (e: Exception){
                _animeList.value = emptyList()
            }
        }
    }
}