package com.example.eksamen.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.eksamen.viewmodel.FavoritesAnimeViewModel
import com.example.eksamen.viewmodel.SortOption




 //Favoritt-skjerm for å vise og administrere lagrede Anime-favoritter.
 //Bruker ViewModel for å hente favoritter og styre sortering.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(viewModel: FavoritesAnimeViewModel = viewModel()) {

    //Henter favoritter og nåværende sorteringsvalg fra ViewModel
    val favorites by viewModel.favorites.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()

    //States for slette alert
    var deleteMessage by remember { mutableStateOf(false) }
    var deleteAnime by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        //Overskrift
        Text(
            text = "Favoritter",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Info tekst
        Text(
            text = "Her er dine favoritter",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Dropdown for å sortere listen
        SortDropdown(
            selectedOption = sortOption,
            onOptionSelected = { viewModel.changeSortOption(it) }
        )

        Spacer(Modifier.height(16.dp))

        if (favorites.isEmpty()) {
            //Viser en melding hvis det ikke finnes favoritter
            Box(
                Modifier.fillMaxSize(), Alignment.Center) {
                Text("Ingen favoritter ennå",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground)
            }
        } else {
            //Liste over alle favoritt-anime som vises som Cards
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(favorites) { anime ->
                    FavoriteAnimeCard(
                        title = anime.title,
                        imageUrl = anime.imageUrl,
                        onDelete = {
                            deleteMessage = true
                            deleteAnime = anime.title
                        }
                    )
                }
            }
        }
    }
    //Slette alert
    if (deleteMessage && deleteAnime != null) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onDismissRequest = {
                deleteMessage = false
                deleteAnime = null
            },
            title = {
                Text("Fjern fra favoritter",
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            },
            text = {
                Text("Er du sikker på at du vil fjerne animeen fra favoritter?",
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            },
            confirmButton = {
                Button(
                    onClick = {
                    val anime = favorites.find { it.title == deleteAnime }
                    if (anime != null) {
                        viewModel.removeFromFavorites(anime)
                    }
                    deleteMessage = false
                    deleteAnime = null
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor= MaterialTheme.colorScheme.onSecondary
                    )
                 ) {
                    Text("Slett")

                }
            },
            dismissButton = {
                TextButton(onClick = {
                    deleteMessage = false
                    deleteAnime = null
                },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor= MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Avbryt")
                }
            }

        )
    }
}


/**
 * Komponent for å vise en anime-favoritt i en Card med bilde og slett-knapp.
 */
@Composable
fun FavoriteAnimeCard(
    title: String,
    imageUrl: String?,
    onDelete: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ){
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            //Bilde av anime
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.small)
            )
            Spacer(Modifier.width(12.dp))

            //Tittel
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            //Slett-knapp
            IconButton(onClick = onDelete){
                Icon(Icons.Default.Delete,
                    contentDescription = "Fjern fra favoritter")
            }
        }
    }
}

// Dropdown som lar brukeren velge hvordan favoritter skal sorteres
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDropdown(selectedOption: SortOption, onOptionSelected: (SortOption) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded}
    ) {
        //Dette ser ut som er teksfelt men fungerer som dropdown-trigger
        TextField(
            value = selectedOption.displayName,
            onValueChange = {},
            readOnly = true,
            label = {Text("Sorter etter")},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
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
        //Dropdown meny
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded=false},
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            SortOption.values().forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option.displayName,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                           },

                        onClick = {
                        onOptionSelected(option)//oppdater sortering
                        expanded = false
                    }
                )
            }
        }

    }
}