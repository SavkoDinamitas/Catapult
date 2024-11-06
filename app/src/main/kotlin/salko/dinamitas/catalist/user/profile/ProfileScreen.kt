package salko.dinamitas.catalist.user.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Score
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import salko.dinamitas.catalist.leaderboard.GlobalLeaderboardViewModel
import salko.dinamitas.catalist.leaderboard.LeaderboardScreen
import salko.dinamitas.catalist.leaderboard.db.LeaderboardData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun NavGraphBuilder.profile(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val state = profileViewModel.state.collectAsState()

    Scaffold {
        paddingValues ->
        UserStatsScreen(
            username = state.value.username,
            highestRanking = state.value.highestRanking,
            highestResult = state.value.highestResult,
            tries = state.value.allResults,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
@Composable
fun UserStatsScreen(
    username: String,
    highestResult: Float,
    highestRanking: Int,
    tries: List<LeaderboardData>,
    modifier: Modifier
    ) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = username,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {

        }
        ListItem(headlineContent = {
            Text(text = "Highest result")
        },
            supportingContent = {
                Text(
                    text = String.format("%.2f", highestResult)
                )
            },
            leadingContent = {

                Icon(Icons.Default.Score, "")
            },
            //colors = ListItemDefaults.colors(MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.clip(
                RoundedCornerShape(10.dp))
        )
        ListItem(headlineContent = {
            Text(text = "Highest ranking")
        },
            supportingContent = {
                Text(
                    text = "$highestRanking."
                )
            },
            leadingContent = {

                Icon(Icons.Default.Scoreboard, "")
            },
            modifier = Modifier.clip(
                RoundedCornerShape(10.dp)))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                TriesHeader()
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)
            }

            items(tries) { singleTry ->
                TryItem(singleTry)
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Composable
fun TriesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Result",
            modifier = Modifier.weight(0.3f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Time Created",
            modifier = Modifier.weight(0.3f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TryItem(singleTry: LeaderboardData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = singleTry.result.toString(),
            modifier = Modifier.weight(0.3f),
            fontSize = 16.sp
        )
        Text(
            text = LocalDateTime.ofInstant(Instant.ofEpochMilli(singleTry.createdAt), ZoneId.systemDefault()).format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)),
            modifier = Modifier.weight(0.3f),
            fontSize = 16.sp
        )
    }
}