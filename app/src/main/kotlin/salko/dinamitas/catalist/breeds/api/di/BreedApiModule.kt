package salko.dinamitas.catalist.breeds.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import salko.dinamitas.catalist.breeds.api.CatApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BreedApiModule {
    @Provides
    @Singleton
    fun providePhotosApi(retrofit: Retrofit): CatApi = retrofit.create()
}