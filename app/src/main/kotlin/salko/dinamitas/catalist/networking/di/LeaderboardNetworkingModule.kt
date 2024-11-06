package salko.dinamitas.catalist.networking.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import salko.dinamitas.catalist.networking.serialization.AppJson
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardNetworkingModule {
    @Singleton
    @Provides
    @Named("leaderboardApi")
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()

            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
    }

    @Singleton
    @Provides
    @Named("leaderboardApi")
    fun provideRetrofitClient(
        @Named("leaderboardApi")okHttpClient: OkHttpClient,
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}