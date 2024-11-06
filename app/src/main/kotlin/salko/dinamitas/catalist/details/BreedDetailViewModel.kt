package salko.dinamitas.catalist.details

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
import salko.dinamitas.catalist.mappers.asBreedApiModel
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class BreedDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepository
):ViewModel() {

    private val breedId: String = savedStateHandle["id"]!!
    private val _state = MutableStateFlow(BreedDetailState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedDetailState.() -> BreedDetailState) = _state.getAndUpdate(reducer)

    init {
        fetchBreedDetails()
        observeBreed()
    }

    private fun fetchBreedDetails(){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val data = withContext(Dispatchers.IO) {
                    repository.getBreed(breedId = breedId)
                }
                setState { copy(data=data.asBreedApiModel()) }
                fetchBreedImage(data.idSlike)
            } catch (error: IOException) {
                setState {
                    copy(error = BreedDetailState.DetailsError.DataUpdateFailed(cause = error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun fetchBreedImage(idSlike: String){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    val image = repository.getImage(idSlike)
                    setState { copy(image = image) }
                }
            } catch (error: IOException) {
                setState {
                    copy(error = BreedDetailState.DetailsError.DataUpdateFailed(cause = error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun observeBreed() {
        viewModelScope.launch {
            repository.observeBreed(breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(data = it.asBreedApiModel(), image = it.image)}
                }
        }
    }
}