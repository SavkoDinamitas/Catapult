package salko.dinamitas.catalist.leaderboard.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LeaderboardData (
    @PrimaryKey(autoGenerate = true)
    val pk: Long = 0,
    val nickname: String,
    val ranking: Int? = null,
    val result: Float,
    val category: Int,
    val createdAt: Long = System.currentTimeMillis()
)