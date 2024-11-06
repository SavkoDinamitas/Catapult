package salko.dinamitas.catalist.di

import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultDispatcherProvider @Inject constructor() : CoroutineDispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun default(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}