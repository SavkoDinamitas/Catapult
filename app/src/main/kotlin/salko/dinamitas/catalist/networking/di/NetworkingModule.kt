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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {
    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            /*
             * Order of okhttp interceptors is important.
             * If logging was first it would not log the custom header.
             */
            .addInterceptor {
                val updatedRequest = it.request().newBuilder()
                    .addHeader("x-api-key", "live_5jaulTQeyJLbfqSYz8xrUdPCJkCv2IFhUNCC4NuXPHqK1t4okceJBI7rkYExVkE1")
                    .build()
                it.proceed(updatedRequest)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
    ) : Retrofit {
        return Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/v1/")
    .client(okHttpClient)
    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
    .build()
    }
}