package salko.dinamitas.catalist.navigation

import androidx.compose.material3.DrawerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import salko.dinamitas.catalist.navigation.states.NavigationEvents
import salko.dinamitas.catalist.quiz.states.QuizEvents
import salko.dinamitas.catalist.user.User
import salko.dinamitas.catalist.user.UserDataStore
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    val dataStore: UserDataStore
) : ViewModel() {

    private val events = MutableSharedFlow<NavigationEvents>()


    fun setEvent(event: NavigationEvents) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is NavigationEvents.register -> {
                        register(it.user)
                    }
                }
            }
        }
    }

    private fun register(user: User){
        viewModelScope.launch {
            dataStore.updateConfig { copy(user = user) }
        }
    }

}
