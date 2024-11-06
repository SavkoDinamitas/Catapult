package salko.dinamitas.catalist.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import salko.dinamitas.catalist.user.CatalistConfig
import salko.dinamitas.catalist.user.User
import salko.dinamitas.catalist.user.UserSerialization
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideCatapultdataStore(
        @ApplicationContext context: Context,
    ): DataStore<CatalistConfig> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile("catapult_config.json") },
            serializer = UserSerialization()
        )
}