package salko.dinamitas.catalist.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import salko.dinamitas.catalist.details.BreedDetailViewModel
import salko.dinamitas.catalist.leaderboard.model.LeaderboardApiModel

fun NavGraphBuilder.globalLeaderboard(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val globalLeaderboardViewModel = hiltViewModel<GlobalLeaderboardViewModel>()
    val state = globalLeaderboardViewModel.state.collectAsState()
    Scaffold { paddingValues ->
        LeaderboardScreen(players = state.value.data, Modifier.padding(paddingValues), state.value)
    }
}

@Composable
fun LeaderboardScreen(
    players: List<LeaderboardApiModel>,
    modifier: Modifier = Modifier,
    state: GlobalLeaderboardState
    ) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Global Leaderboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                LeaderboardHeader()
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)
            }
            itemsIndexed(players) { index, player ->
                LeaderboardItem(
                    ordinalNumber = index + 1,
                    player = player,
                    gamesPlayed = state.gamesPlayed[player.nickname]?: throw IllegalStateException("no lol")
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Composable
fun LeaderboardItem(ordinalNumber: Int, player: LeaderboardApiModel, gamesPlayed: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ordinalNumber.toString(),
            modifier = Modifier.weight(0.1f),
            fontSize = 16.sp
        )
        Text(
            text = player.nickname,
            modifier = Modifier.weight(0.5f),
            fontSize = 16.sp
        )
        Text(
            text = String.format("%.2f", player.result),
            modifier = Modifier.weight(0.3f),
            fontSize = 16.sp
        )
        Text(
            text = gamesPlayed.toString(),
            modifier = Modifier.weight(0.3f),
            fontSize = 16.sp
        )
    }
}

@Composable
fun LeaderboardHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "No.",
            modifier = Modifier.weight(0.1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Nickname",
            modifier = Modifier.weight(0.5f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Result",
            modifier = Modifier.weight(0.3f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Games",
            modifier = Modifier.weight(0.3f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
/*
@Preview
@Composable
fun PreviewLeaderboardScreen() {
    val samplePlayers = listOf(
        LeaderboardApiModel(nickname = "Player1", result = 100f, category = 3),
        LeaderboardApiModel(nickname = "Player2", result = 90f, category = 3),
        LeaderboardApiModel(nickname = "Player3", result = 85f, category = 3),
        LeaderboardApiModel(nickname = "Player4", result = 70f, category = 3),
        LeaderboardApiModel(nickname = "Player5", result = 65f, category = 3)
    )

    LeaderboardScreen(players = samplePlayers, )
}*/