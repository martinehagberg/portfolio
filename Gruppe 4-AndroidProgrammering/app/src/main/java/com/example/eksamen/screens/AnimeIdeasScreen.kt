package com.example.eksamen.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eksamen.model.local.AnimeIdea
import com.example.eksamen.repository.AnimeRepository
import com.example.eksamen.screens.component.AnimeIdeaItem
import com.example.eksamen.viewmodel.AnimeIdeaViewModel
import com.example.eksamen.viewmodel.AnimeIdeaViewModelFactory

// Liste med mulige sjangere - lagres utenfor composable så de ikke re-initialiseres
private val genreOptions = listOf( "Action", "Drama", "Comedy", "Fantasy", "Sci-Fi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeIdeasScreen(
    viewModel: AnimeIdeaViewModel = viewModel(
        factory = AnimeIdeaViewModelFactory(AnimeRepository.animeIdeasDao)
    )
) {
    //Henter listen av ideer som en StateFlow -> Compose oppdateres automatisk når databasen endres
    val ideas by viewModel.ideas.collectAsState()

    //Holder styr på hvilken deler av UI som er aktivt
    var editIdea by remember { mutableStateOf<AnimeIdea?>(null) }
    var selectedIdea by remember { mutableStateOf<AnimeIdea?>(null) }

    //Inputfelter for tittel, beskrivelse og sjanger
    var title by rememberSaveable() { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }

    //Styrer om sjanger-dropdown er åpen
    var expanded by remember {mutableStateOf(false)}

    //Bekreft sletting
    var deleteMessage by remember { mutableStateOf(false) }
    var deleteIdea by remember { mutableStateOf<AnimeIdea?>(null) }



    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        //Oversfkrift
        Text(
            text = "Dine anime ideer",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Info tekst
        Text(
            text = "Lag dine egne anime ideer",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Input tittel
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tittel") },
            modifier = Modifier.fillMaxWidth(),
            //Tilpassede farger for å matche appens tema
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,

            )

        )

        Spacer(Modifier.height(8.dp))

        //Input beskrivelse
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Beskrivelse") },
            modifier = Modifier.fillMaxWidth(),
            //Tilpassede farger for å matche appens tema
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
            )
        )

        Spacer(Modifier.height(16.dp))

        //Sjanger dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded = !expanded}
        ) {
            TextField(
                value = selectedGenre,
                onValueChange = {},
                readOnly = true, //viktig: brukeren skal ikke taste, kun velge
                label = {Text("Velg sjanger")},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded)},
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

            //Selve menyen med alternativer
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false},
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                genreOptions.forEach{ genre ->
                    DropdownMenuItem(
                        text = { Text(genre,
                            color = MaterialTheme.colorScheme.onPrimaryContainer) },
                        onClick = {
                            selectedGenre = genre
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        //Legg til / Lagre knapp
        Button(
            onClick = {
                if (title.isNotBlank() || description.isNotBlank()) {
                    if (editIdea == null) {
                        //Ny ide legegs inn i databasen
                        viewModel.addIdea(title, description, selectedGenre)
                    } else {
                        //Kopierer ideen med oppdaterte verdier
                        val updated = editIdea!!.copy(
                            title = title,
                            description = description,
                            genre = selectedGenre
                            )
                        //Oppdaterer i databasen
                        viewModel.updateIdea(updated)
                        //Hvis brukeren er på denne ideen -> oppdater detaljene også
                        if(selectedIdea?.id == updated.id){
                            selectedIdea = updated
                        }
                        editIdea = null
                    }
                    //Tilbakestill skjema
                    title = ""
                    description = ""
                    selectedGenre = ""
                }
            },

            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor= MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(if (editIdea == null) "Legg til" else "Lagre")
        }

        Spacer(Modifier.height(16.dp))

        //Liste over alle ideer
        LazyColumn {
            items(ideas) { idea ->
                AnimeIdeaItem(
                    idea = idea,
                    onClick = {
                        selectedIdea = idea
                        editIdea = null//Hvis man ser på detaljer skal ikke redigering være aktiv
                    },
                    onEdit = {
                        editIdea = idea
                        title = idea.title
                        description = idea.description
                        selectedGenre = idea.genre
                    },
                    onDelete = { deleteIdea = idea
                                deleteMessage = true},
                    onDeleteSelected = {
                        if (selectedIdea == idea) selectedIdea = null
                    }
                )
            }
        }



        // Detaljvisning av valgt ide

        selectedIdea?.let { idea ->
            Spacer(Modifier.height(20.dp))

            Text(
                text = idea.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(text = "Beskrivelse: ${idea.description}")
            Text(text = "Sjanger: ${idea.genre}")

        }
        //Slette alert
        if (deleteMessage && deleteIdea != null) {
            AlertDialog(
                onDismissRequest = {
                    deleteMessage = false
                    deleteIdea = null },
                containerColor = MaterialTheme.colorScheme.primaryContainer,

                title = {
                    Text(
                        "Slett ide",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                        },
                text = {
                    Text(
                        "Er du sikker på at du vil slette denne ideen?",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                       },
                confirmButton = {
                    Button(
                        onClick = {
                        viewModel.deleteIdea(deleteIdea!!)
                        deleteMessage = false
                        deleteIdea = null
                    },
                        colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary)
                    ) {
                        Text("Slett")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        deleteMessage = false
                        deleteIdea = null
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary)) {
                        Text("Avbryt")

                    }
                }
            )

        }
    }
}



