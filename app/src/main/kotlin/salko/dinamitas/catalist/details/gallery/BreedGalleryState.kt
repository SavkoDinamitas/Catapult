package salko.dinamitas.catalist.details.gallery

import salko.dinamitas.catalist.breeds.api.model.BreedApiModel
import salko.dinamitas.catalist.breeds.db.Image

data class BreedGalleryState(
    val breedId: String,
    val fetching: Boolean = false,
    val data: List<Image>? = null,
    val error: GalleryError? = null,
){
    sealed class GalleryError {
        data class DataUpdateFailed(val cause: Throwable? = null) : GalleryError()
    }
}