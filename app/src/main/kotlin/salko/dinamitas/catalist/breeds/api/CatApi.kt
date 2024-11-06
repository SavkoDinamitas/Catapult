package salko.dinamitas.catalist.breeds.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import salko.dinamitas.catalist.breeds.api.model.BreedApiModel
import salko.dinamitas.catalist.breeds.api.model.Image

interface CatApi {
    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>

    @GET("breeds/{id}")
    suspend fun getBreed(@Path("id") breedId: String): BreedApiModel

    @GET("images/{id}")
    suspend fun getImage(@Path("id") imageId: String) : Image

    @GET("images/search?limit=25")
    suspend fun getBreedImages(@Query("breed_ids") breedId: String) : List<salko.dinamitas.catalist.breeds.db.Image>
}