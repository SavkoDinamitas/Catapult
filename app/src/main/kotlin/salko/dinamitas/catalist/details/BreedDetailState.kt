package salko.dinamitas.catalist.details

import salko.dinamitas.catalist.breeds.api.model.BreedApiModel
import salko.dinamitas.catalist.breeds.api.model.Image

data class BreedDetailState(
    val breedId: String,
    val fetching: Boolean = false,
    val data: BreedApiModel? = null,
    val error: DetailsError? = null,
    val image: Image? = null
){
    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}