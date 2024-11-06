package salko.dinamitas.catalist.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
@Dao
interface BreedDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(breedData: Breed)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Breed>)

    @Query("SELECT * FROM Breed")
    suspend fun getAll(): List<Breed>
    @Query("select * from Breed where id = :breedId")
    suspend fun getBreed(breedId: String): Breed

    @Query("select * from Breed where id = :breedId")
    suspend fun getImages(breedId:String):ImagesForBreed

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(breedData: Image)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllImages(list: List<Image>)

    @Transaction
    @Query(
        """
            select * from breed
        """
    )
    fun observeAllBreeds(): Flow<List<Breed>>
    @Query(
        "select * from Breed where Breed.id = :id"
    )
    fun observeBreed(id: String): Flow<Breed>
    @Query("select * from image where Image.breedId = :breedId")
    fun observeImages(breedId:String) : Flow<List<Image>>

}