package com.example.eksamen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eksamen.model.local.AnimeIdeasDao

//Factory klasse for å opprette AnimeIdeaViewModel med et Dao-argument
class AnimeIdeaViewModelFactory(
    private val dao: AnimeIdeasDao
) : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {

            //Sjekker at requested ViewModel-type stemmer
            if (modelClass.isAssignableFrom(AnimeIdeaViewModel::class.java)){
                return AnimeIdeaViewModel(dao) as T
            }

            //Hvis en annen type etterspørres, gi tydelig feilmelding
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}