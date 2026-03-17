package com.example.eksamen.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.eksamen.model.local.FavoritesAnime
import com.example.eksamen.viewmodel.AnimeViewModel
import com.example.eksamen.viewmodel.FavoritesAnimeViewModel


// Skjerm som viser alle anime hentet fra API-et
//Med filtrering, visning av liste og mulighet for å legge til favoritter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAnimeScreen(
    viewModel: AnimeViewModel = viewModel(),
    favoritesViewModel: FavoritesAnimeViewModel
) {
    //Henter liste fra ViewModel og gjør den observerbar i Compose
    val animeList by viewModel.animeList.collectAsState()

    //Laster anime en gang når skjermen åpnes
    LaunchedEffect(Unit) { viewModel.loadAnime() }

    var expanded by remember {mutableStateOf(false)}
    var selectedGenre by remember { mutableStateOf("Alle") }
    val genreOptions = listOf("Alle", "Action", "Drama", "Comedy", "Fantasy", "Sci-Fi")

    //Filtrering basert på valgt sjanger
    val filteredAnimeList =
        if(selectedGenre == "Alle") animeList
        else animeList.filter { it.genres?.contains(selectedGenre,ignoreCase = true) == true}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Overskrift
        Text(
            text = "Alle",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Viser antall anime
        Text(
            text = "Antall anime: ${filteredAnimeList.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(8.dp))


        //Sjanger dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded = !expanded}
        ) {
            TextField(
                value = selectedGenre,
                onValueChange = {},
                readOnly = true,
                label = {Text("Filtrer etter sjanger")},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
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
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false},
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                genreOptions.forEach{ genre ->
                    DropdownMenuItem(
                        text = { Text(genre,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)},
                        onClick = {
                            selectedGenre = genre
                            expanded = false
                        }
                    )
                }
            }
        }

        //Anime liste
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(filteredAnimeList) { anime ->
                var expandedCard by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer

                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        // Anime bilde
                        AsyncImage(
                            model = anime.imageUrl,
                            contentDescription = anime.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tittel på anime
                        Text(text = anime.title, style = MaterialTheme.typography.displaySmall)

                        //Rating, status, episoder og sjangere
                        Text(text = "Score: ${anime.score ?: "Ukjent"}", fontWeight = FontWeight.Bold)
                        Text(text = "Episoder: ${anime.episodes ?: "Ukjent"}", fontWeight = FontWeight.Bold)

                        anime.genres?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Sjangere: $it", fontWeight = FontWeight.Bold)
                        }

                        // Synopsis(les mer eller mindre)
                        anime.synopsis?.let { synopsis ->
                            val textToShow = if (expandedCard) synopsis else synopsis.take(120) + ""
                                Text(
                                    text = textToShow,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (expandedCard) "Se mindre" else "Se mer",
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .clickable { expandedCard = !expandedCard }
                                .padding(8.dp)
                        )

                        //Favoritt knapp
                        IconButton(
                            onClick = {
                                favoritesViewModel.addToFavorites(
                                    FavoritesAnime(
                                        id = anime.id,
                                        title = anime.title,
                                        imageUrl = anime.imageUrl
                                    )
                                )
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Add to favorites",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}