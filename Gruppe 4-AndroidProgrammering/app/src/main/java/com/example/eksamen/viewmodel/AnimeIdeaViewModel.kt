package com.example.eksamen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eksamen.model.local.AnimeIdea
import com.example.eksamen.model.local.AnimeIdeasDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//ViewModel som håndterer CRUD-operasjoner på brukerens anime-ideer
class AnimeIdeaViewModel(private val dao: AnimeIdeasDao) : ViewModel() {

    //Stateflow til å holde data fra databasen
    val ideas = dao.getAllIdeas().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    //Legger til ny ide i databasen
    fun addIdea(title: String, desc: String, genre: String){
        viewModelScope.launch {
            dao.insertIdea(AnimeIdea(title = title, description = desc, genre = genre))
        }
    }

    //Oppdaterer objekt i databasen
    fun updateIdea(idea: AnimeIdea){
        viewModelScope.launch {
            dao.updateIdea(idea)
        }
    }

    //Sletter objekt i databasen
    fun deleteIdea(idea: AnimeIdea){
        viewModelScope.launch {
            dao.deleteIdea(idea)
        }

    }

}