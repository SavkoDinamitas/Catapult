package salko.dinamitas.catalist.details.gallery

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import salko.dinamitas.catalist.breeds.repository.BreedRepository
import salko.dinamitas.catalist.details.BreedDetailState
import salko.dinamitas.catalist.mappers.asBreedApiModel
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreedGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val breedRepository: BreedRepository
):ViewModel() {
    private val breedId: String = savedStateHandle["breedId"]!!
    private val _state = MutableStateFlow(BreedGalleryState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedGalleryState.() -> BreedGalleryState) = _state.getAndUpdate(reducer)

    init {
        fetchBreedImages()
        observeBreedImages()
    }

    private fun fetchBreedImages(){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    breedRepository.getBreedImages(breedId = breedId)
                }
            } catch (error: IOException) {
                setState {
                    copy(error = BreedGalleryState.GalleryError.DataUpdateFailed(cause = error))
                }
            } finally {
                Log.d("mast", "ddx")
                setState { copy(fetching = false) }
            }
        }
    }

    private fun observeBreedImages() {
        viewModelScope.launch {
            breedRepository.observeBreedImages(breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(data = it)}
                }
        }
    }

}