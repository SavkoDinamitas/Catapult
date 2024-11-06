package salko.dinamitas.catalist.breeds.repository


import androidx.room.withTransaction
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import retrofit2.HttpException
import salko.dinamitas.catalist.breeds.api.CatApi
import salko.dinamitas.catalist.breeds.db.Breed
import salko.dinamitas.catalist.breeds.db.ImagesForBreed
import salko.dinamitas.catalist.db.AppDatabase
import salko.dinamitas.catalist.mappers.asBreedDbModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedRepository @Inject constructor(
    private val breedApi: CatApi,
    private val database: AppDatabase
) {
    private val keySet = mutableSetOf<String>()

    private val imageSet = mutableSetOf<String>()

    suspend fun getAllBreeds(){
        val allBreeds = breedApi.getAllBreeds().map{it.asBreedDbModel()}.toMutableList()

        database.withTransaction {
            database.breedDao().insertAll(allBreeds);
        }
    }

    fun observeBreeds() = database.breedDao().observeAllBreeds()

    fun observeBreed(id: String) = database.breedDao().observeBreed(id)

    fun observeBreedImages(breedId: String) = database.breedDao().observeImages(breedId = breedId)

    suspend fun getBreed(breedId: String):Breed{
        if(keySet.contains(breedId)){
            return database.breedDao().getBreed(breedId)
        }
        else{
            keySet.add(breedId)
            return breedApi.getBreed(breedId = breedId).asBreedDbModel()

        }
    }

    suspend fun getImage(imageId: String) = breedApi.getImage(imageId = imageId)

    suspend fun getBreedImages(breedId: String): ImagesForBreed {
        if(!imageSet.contains(breedId)){
            imageSet.add(breedId)
            while(true){
                try{
                    val breedImages = breedApi.getBreedImages(breedId = breedId).map { it.copy(breedId = breedId) }
                    database.withTransaction {
                        database.breedDao().insertAllImages(breedImages);
                    }
                    break
                }catch (error: HttpException){
                    if(error.code() == 429){
                        delay(1000);
                        continue;
                    }
                    else{
                        throw error
                    }
                }
            }

        }
        return database.breedDao().getImages(breedId)
    }
}