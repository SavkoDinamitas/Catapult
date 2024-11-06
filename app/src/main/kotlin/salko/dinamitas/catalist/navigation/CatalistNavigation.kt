package salko.dinamitas.catalist.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import salko.dinamitas.catalist.breeds.list.breedsListScreen
import salko.dinamitas.catalist.details.breedDetailsScreen
import salko.dinamitas.catalist.details.gallery.breedGalleryScreen
import salko.dinamitas.catalist.leaderboard.globalLeaderboard
import salko.dinamitas.catalist.login.loginScreen
import salko.dinamitas.catalist.quiz.quizEnd.quizEndScreen
import salko.dinamitas.catalist.quiz.quizScreen
import salko.dinamitas.catalist.user.CatalistConfig
import salko.dinamitas.catalist.user.editUserScreen
import salko.dinamitas.catalist.user.profile.profile

@Composable
fun CatalistNavigation(){
    val navController = rememberNavController()
    val navigationViewModel = hiltViewModel<NavigationViewModel>()
    val state by navigationViewModel.dataStore.config.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val corutineScope = rememberCoroutineScope()

    val startDestination = if(state.user == null) "login" else "breeds"


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navController, drawerState, state, corutineScope)
            }
        },
        content = {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ){
                breedsListScreen(
                    route = "breeds",
                    navController = navController)
                breedDetailsScreen(
                    route = "breeds/{id}",
                    navController = navController,
                )
                breedGalleryScreen(
                    route = "gallery/{breedId}",
                    navController = navController
                )
                loginScreen(
                    route = "login",
                    navController = navController
                )
                editUserScreen(
                    route = "edit",
                    navController = navController
                )
                quizScreen(
                    route = "quiz",
                    navController = navController
                )
                quizEndScreen(
                    route = "quizEnd/{finalScore}",
                    navController = navController
                )
                globalLeaderboard(
                    route = "globalLeaderboard",
                    navController = navController
                )
                profile(
                    route = "profile",
                    navController = navController
                )
            }
        }
    )
}


@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState, state: CatalistConfig, coroutineScope: CoroutineScope) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            state.user?.let {
                Text(
                    text = it.username,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            HorizontalDivider()

            DrawerItem(
                icon = Icons.Default.Home,
                label = "Home",
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route = "breeds")
                }
            )

            state.user?.let {
                DrawerItem(
                    icon = Icons.Default.Person,
                    label = it.firstName + " " + it.lastName,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navController.navigate(route = "profile")
                    }
                )
            }
            DrawerItem(
                icon = Icons.Default.Edit,
                label = "Edit Profile",
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route = "edit")
                }
            )
            DrawerItem(
                icon = Icons.Default.PlayArrow,
                label = "Start quiz",
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route = "quiz")
                }
            )

            DrawerItem(
                icon = Icons.Default.Leaderboard,
                label = "Global Leaderboard",
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route = "globalLeaderboard")
                }
            )
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(text = label, color = MaterialTheme.colorScheme.secondary)
    }
}