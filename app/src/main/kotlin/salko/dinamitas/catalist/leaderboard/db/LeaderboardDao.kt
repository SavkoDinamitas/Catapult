package salko.dinamitas.catalist.leaderboard.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import salko.dinamitas.catalist.breeds.db.Breed

@Dao
interface LeaderboardDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(leaderboardData: LeaderboardData):Long

    @Query("UPDATE leaderboarddata SET ranking = :ranking WHERE pk = :resultKey")
    suspend fun updateRanking(resultKey:Long, ranking:Int)

    @Query("select * from leaderboarddata order by createdAt desc")
    suspend fun getAllResults(): List<LeaderboardData>

    @Query("select min(ranking) from leaderboarddata")
    suspend fun getHighestRanking():Int

    @Query("select max(result) from leaderboarddata")
    suspend fun getHighestResult():Float

    @Query("select * from leaderboarddata")
    fun observeAllStandings(): Flow<List<LeaderboardData>>
}