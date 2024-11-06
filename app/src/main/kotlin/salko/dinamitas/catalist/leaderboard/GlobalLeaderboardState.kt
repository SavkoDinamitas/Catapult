package salko.dinamitas.catalist.leaderboard

import salko.dinamitas.catalist.details.BreedDetailState
import salko.dinamitas.catalist.leaderboard.model.LeaderboardApiModel


data class GlobalLeaderboardState(
    val fetching: Boolean = false,
    val data: List<LeaderboardApiModel> = listOf(),
    val error: LeaderboardError? = null,
    val gamesPlayed: Map<String, Int> = mapOf()
){
    sealed class LeaderboardError{
        data class DataFetchFailed(val cause: Throwable? = null) : LeaderboardError()
    }
}