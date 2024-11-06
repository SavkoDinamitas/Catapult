package salko.dinamitas.catalist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {
    @Singleton
    @Binds
    abstract fun dispatcherProvider(arg: DefaultDispatcherProvider):CoroutineDispatcherProvider
}