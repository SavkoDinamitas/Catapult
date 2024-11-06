package salko.dinamitas.catalist.user

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import salko.dinamitas.catalist.di.CoroutineDispatcherProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class UserDataStore @Inject constructor(
    dispatcherProvider: CoroutineDispatcherProvider,
    private val persistence: DataStore<CatalistConfig>
){
    private val scope = CoroutineScope(dispatcherProvider.io())

    val config = persistence.data
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { persistence.data.first() }
        )

    suspend fun updateConfig(reducer: CatalistConfig.() -> CatalistConfig){
        persistence.updateData { it.reducer() }
    }
}