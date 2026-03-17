package com.example.eksamen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eksamen.navigation.Screen
import com.example.eksamen.repository.AnimeRepository
import com.example.eksamen.screens.AllAnimeScreen
import com.example.eksamen.screens.AnimeByIdScreen
import com.example.eksamen.screens.FavoritesScreen
import com.example.eksamen.screens.AnimeIdeasScreen
import com.example.eksamen.viewmodel.FavoritesAnimeViewModel


@Composable
fun AnimeApp() {

    // navigation controller
    val navController = rememberNavController()
    // DAO fra Room Database via Repository
    val dao = AnimeRepository.getDatabase().animeDao()
    val favoritesAnimeViewModel = FavoritesAnimeViewModel(dao)


    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            //Hjem
            composable(Screen.Home.route) {
                AllAnimeScreen(favoritesViewModel = favoritesAnimeViewModel)
            }
            //Søke
            composable(Screen.Search.route) {
                AnimeByIdScreen(id = null,
                    navController = navController,
                    favoritesAnimeViewModel = favoritesAnimeViewModel
                    )
            }
            //Ideer
            composable(Screen.Ideas.route) {
                AnimeIdeasScreen()
            }
            //favoritter
            composable(Screen.Favorites.route) {
                FavoritesScreen(viewModel = favoritesAnimeViewModel)

            }
        }
    }
}

// Bottom navigation
@Composable
fun BottomNavBar(navController: NavHostController) {
    // hvilke skjermer som skal vises i menyen
    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Ideas,
        Screen.Favorites
    )

    // finn nåverende rute( for å markere valgt knapp)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute?.startsWith(screen.route) == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Text(text = screen.label)
                },
                label = null,
                alwaysShowLabel = false
            )
        }
    }
}