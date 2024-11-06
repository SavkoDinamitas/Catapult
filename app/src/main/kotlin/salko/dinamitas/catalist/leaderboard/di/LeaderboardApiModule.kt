package salko.dinamitas.catalist.leaderboard.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import salko.dinamitas.catalist.leaderboard.api.LeaderboardApi
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardApiModule {
    @Provides
    @Singleton
    fun providePhotosApi(@Named("leaderboardApi")retrofit: Retrofit): LeaderboardApi = retrofit.create()
}