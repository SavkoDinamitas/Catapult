package salko.dinamitas.catalist.leaderboard.model

import kotlinx.serialization.Serializable
import salko.dinamitas.catalist.networking.serialization.RmaODTSerializer
import java.time.OffsetDateTime

@Serializable
data class LeaderboardApiModel(
    val category: Int,
    val nickname: String,
    val result: Float,
    @Serializable(with = RmaODTSerializer::class)
    val createdAt: OffsetDateTime? = null,
    val gameNumber: Int? = 0
)
@Serializable
data class LeaderboardApiResponse(
    val result: LeaderboardApiModel,
    val ranking: Int
)
