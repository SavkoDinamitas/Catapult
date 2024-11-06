package salko.dinamitas.catalist.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import salko.dinamitas.catalist.leaderboard.GlobalLeaderboardState
import salko.dinamitas.catalist.leaderboard.repository.LeaderboardRepository
import salko.dinamitas.catalist.user.UserDataStore
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
    private val dataStore: UserDataStore
): ViewModel() {
    private val username = dataStore.config.value.user?.username ?: ""
    private val _state = MutableStateFlow(ProfileState(username = username))
    val state = _state.asStateFlow()
    private fun setState(reducer: ProfileState.() -> ProfileState) = _state.getAndUpdate(reducer)

    init {
        getResults()
        getHighestResult()
        getHighestRanking()
    }

    private fun getResults(){

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                setState { copy(fetching = true) }
                val results = leaderboardRepository.getMyResults()
                setState { copy(allResults = results, fetching = false) }
            }
        }
    }

    private fun getHighestResult(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                setState { copy(fetching = true) }
                val bestResult = leaderboardRepository.getBestResult()
                setState { copy(highestResult = bestResult, fetching = false) }
            }
        }
    }

    private fun getHighestRanking(){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            withContext(Dispatchers.IO){
                val bestRanking = leaderboardRepository.getBestRanking()
                setState { copy(highestRanking = bestRanking, fetching = false) }
            }
        }
    }
}