package salko.dinamitas.catalist.di

import kotlinx.coroutines.CoroutineDispatcher


interface CoroutineDispatcherProvider {
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}