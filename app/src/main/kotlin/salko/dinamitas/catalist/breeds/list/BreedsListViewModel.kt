package salko.dinamitas.catalist.breeds.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import salko.dinamitas.catalist.breeds.list.states.BreedsListEvents
import salko.dinamitas.catalist.breeds.list.states.BreedsListState
import salko.dinamitas.catalist.breeds.repository.BreedRepository
import salko.dinamitas.catalist.mappers.asBreedApiModel
import javax.inject.Inject
@HiltViewModel
class BreedsListViewModel @Inject constructor(
    private val repository: BreedRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BreedsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedsListState.() -> BreedsListState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<BreedsListEvents>()
    fun setEvent(event: BreedsListEvents) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        fetchBreeds()
        observeBreeds()
        observeEvents()
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.getAllBreeds()
                }
            } catch (error: Exception) {
                Log.d("RMA", "Exception", error)
            }
            setState { copy(fetching = false) }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            repository.observeBreeds()
                .distinctUntilChanged()
                .collect {
                    setState { copy(breeds = it.map { it.asBreedApiModel() }) }
                }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is BreedsListEvents.BreedSearch -> {
                        potragaZaNemom(query = it.query)
                    }
                }
            }
        }
    }

    private fun potragaZaNemom(query : String){
        setState { copy(filtered = breeds.filter { it.name.lowercase().contains(query.lowercase()) })}
    }
}