package com.example.eksamen.screens.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eksamen.model.local.AnimeIdea


//En gjenbrukbar rad-komponent som viser en animeide i listen
@Composable
fun AnimeIdeaItem(
    idea: AnimeIdea,
    onClick: () -> Unit, //Detaljer om ideen
    onDelete: () -> Unit,//Sletter fra databasen
    onEdit: () -> Unit = {}, //Gjør det mulig å redigere ideen
    onDeleteSelected: () -> Unit = {} //Rydder opp i UI hvis valgt ide blir slettet
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ onClick() }
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Viser tittelen på idden
        Text(
            text = idea.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        //Slette knapp, kaller både onDelete(DB), onDeleteSelected(rydder UI)
        IconButton(onClick = {
            onDelete()
            onDeleteSelected()
        }) {
            Icon(Icons.Default.Delete,
                contentDescription = "Slett ide")
        }

        //Rediger knapp
        IconButton(onClick = onEdit){
            Icon(Icons.Default.Edit,
                contentDescription = "Rediger")
        }

    }
}


