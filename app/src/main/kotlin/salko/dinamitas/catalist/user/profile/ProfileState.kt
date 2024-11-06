package salko.dinamitas.catalist.user.profile

import salko.dinamitas.catalist.leaderboard.db.LeaderboardData

data class ProfileState(
    val highestRanking: Int = -1,
    val highestResult: Float = -1f,
    val allResults: List<LeaderboardData> = listOf(),
    val fetching: Boolean = false,
    val username: String
)