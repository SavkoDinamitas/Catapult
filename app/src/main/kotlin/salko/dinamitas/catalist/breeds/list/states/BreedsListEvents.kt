package salko.dinamitas.catalist.breeds.list.states

sealed class BreedsListEvents {
    data class BreedSearch(val query : String) : BreedsListEvents()
}