package com.example.eksamen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen.model.Anime
import com.example.eksamen.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//ViewModel for å hente detaljer om en anime via ID
class AnimeByIdViewModel : ViewModel() {
    private val _anime = MutableStateFlow<Anime?>(null)
    val anime = _anime.asStateFlow()

    //Laster inn anime basert på gitt ID
    fun loadAnimeById(id: Int) {
        viewModelScope.launch{
            try {
                _anime.value = AnimeRepository.getAnimeById(id)
            }catch (e: Exception){
                _anime.value = null
            }

        }
    }
}