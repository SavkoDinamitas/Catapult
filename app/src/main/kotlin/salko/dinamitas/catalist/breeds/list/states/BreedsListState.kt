package salko.dinamitas.catalist.breeds.list.states

import salko.dinamitas.catalist.breeds.api.model.BreedApiModel

data class BreedsListState (
    val fetching: Boolean = false,
    val breeds: List<BreedApiModel> = emptyList(),
    val error: ListError? = null,
    val filtered : List<BreedApiModel> = emptyList()
    ) {
        sealed class ListError {
            data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
        }
}