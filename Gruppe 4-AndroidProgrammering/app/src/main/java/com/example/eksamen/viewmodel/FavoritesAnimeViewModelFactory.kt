package com.example.eksamen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eksamen.model.local.AnimeDao

//Factory klasse for å kunne opprette FavoritesAnimeViewModel med et DAO-argument
class FavoritesAnimeViewModelFactory (
    private val dao: AnimeDao): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>):T {
            //Sjekker at ViewModel-typen som forespørres er korrekt
            if (modelClass.isAssignableFrom(FavoritesAnimeViewModel::class.java)){
                return FavoritesAnimeViewModel(dao) as T
            }

            //Gir en tydelig og trygg feilemlding hvis noen prøver å lage feil type ViewModel
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
