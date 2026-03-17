package com.example.eksamen.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.eksamen.model.Anime
import com.example.eksamen.model.local.FavoritesAnime
import com.example.eksamen.viewmodel.AnimeByIdViewModel
import com.example.eksamen.viewmodel.FavoritesAnimeViewModel



//Skjerm som viser anime etter ID, henter data fra API via ViewModel,og mulighet for å legge ti favoritter
@Composable
fun AnimeByIdScreen(
    id: Int?,
    navController: NavController,
    favoritesAnimeViewModel: FavoritesAnimeViewModel,
    viewModel: AnimeByIdViewModel = viewModel()
) {
    var inputId by remember { mutableStateOf("") }
    val anime by viewModel.anime.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        //Overskift
        Text(
            text = "Søk",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Søk etter anime med ID",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline)

        Spacer(modifier = Modifier.height(8.dp))

        //Inputfelt for anime-ID
        OutlinedTextField(
            value = inputId,
            onValueChange = { inputId = it },
            label = { Text("Skriv inn anime ID") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Søk knapp
        Button(
            onClick = {
                val id = inputId.toIntOrNull()
                if (id != null) {
                    viewModel.loadAnimeById(id)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor= MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text("Hent anime")
        }
        Spacer(modifier = Modifier.height(16.dp))

        //Resultat / anime-detaljer
        if (anime != null) {
            AnimeDetails(anime!!, favoritesAnimeViewModel)
        } else {
            Text("Ingen anime funnet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

//Viser detaljer info om en anime
@Composable
fun AnimeDetails(anime: Anime,
                 favoritesAnimeViewModel: FavoritesAnimeViewModel){

        Column(
            modifier = Modifier
                .padding(top=8.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            //Bilde
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier =  Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            //Tittel + favoritt-knapp
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        favoritesAnimeViewModel.addToFavorites(
                            FavoritesAnime(
                                id = anime.id,
                                title = anime.title,
                                imageUrl = anime.imageUrl
                            )
                        )
                    }
                ) {
                    Icon(Icons.Default.Favorite,
                        contentDescription = "Legg til favoritter",
                        tint = MaterialTheme.colorScheme.secondary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //Score og episoder
            Text(text = "Score: ${anime.score ?: "Ukjent"}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline)


            Text(text = "Episoder: ${anime.episodes ?: "Ukjent"}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary)

            //Sjangere
            anime.genres?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Sjangere: $it",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(8.dp))


            //Beskrivelse
            Text(text = anime.synopsis ?: "Ingen beskrivelse tilgjengelig",
            style = MaterialTheme.typography.bodyMedium
            )
        }
    }
