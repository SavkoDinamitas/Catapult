package salko.dinamitas.catalist.leaderboard.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import salko.dinamitas.catalist.leaderboard.model.LeaderboardApiModel
import salko.dinamitas.catalist.leaderboard.model.LeaderboardApiResponse

interface LeaderboardApi {
    @GET("leaderboard?category=3")
    suspend fun getStandings(): List<LeaderboardApiModel>

    @POST("leaderboard")
    suspend fun postResult(@Body leaderboardApiModel: LeaderboardApiModel): LeaderboardApiResponse
}