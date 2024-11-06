package salko.dinamitas.catalist.leaderboard.repository

import salko.dinamitas.catalist.db.AppDatabase
import salko.dinamitas.catalist.leaderboard.api.LeaderboardApi
import salko.dinamitas.catalist.leaderboard.model.LeaderboardApiModel
import salko.dinamitas.catalist.leaderboard.db.LeaderboardData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
    private val database: AppDatabase
) {
    suspend fun getStandings() = leaderboardApi.getStandings()

    suspend fun postResult(leaderboardApiModel: LeaderboardApiModel) = leaderboardApi.postResult(leaderboardApiModel)

    suspend fun saveResult(leaderboardData: LeaderboardData) = database.leaderboardDao().insert(leaderboardData)

    suspend fun updateGlobalRanking(pk:Long, ranking:Int) = database.leaderboardDao().updateRanking(pk, ranking)

    suspend fun getMyResults() = database.leaderboardDao().getAllResults()

    suspend fun getBestRanking() = database.leaderboardDao().getHighestRanking()

    suspend fun getBestResult() = database.leaderboardDao().getHighestResult()
}