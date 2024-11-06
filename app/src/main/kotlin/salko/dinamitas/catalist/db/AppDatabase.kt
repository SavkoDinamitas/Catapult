package salko.dinamitas.catalist.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import salko.dinamitas.catalist.breeds.db.Breed
import salko.dinamitas.catalist.breeds.db.BreedDao
import salko.dinamitas.catalist.breeds.db.Image
import salko.dinamitas.catalist.leaderboard.db.LeaderboardDao
import salko.dinamitas.catalist.leaderboard.db.LeaderboardData

@Database(
    entities = [
        Breed::class,
        Image::class,
        LeaderboardData::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breedDao() : BreedDao
    abstract fun leaderboardDao() : LeaderboardDao
}