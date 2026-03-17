package com.example.eksamen.navigation

//Representerer alle skjermene i appens navigasjonssystem
enum class Screen(val route: String, val label: String) {
    Home("home","Alle"),
    Search("search","Søk"),
    Ideas("ideas","Ideer"),
    Favorites("favorites","Favoritter");

}