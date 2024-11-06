package salko.dinamitas.catalist.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import salko.dinamitas.catalist.leaderboard.repository.LeaderboardRepository
import salko.dinamitas.catalist.quiz.states.QuizState
import javax.inject.Inject

@HiltViewModel
class GlobalLeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
):ViewModel() {
    private val _state = MutableStateFlow(GlobalLeaderboardState())
    val state = _state.asStateFlow()
    private fun setState(reducer: GlobalLeaderboardState.() -> GlobalLeaderboardState) = _state.getAndUpdate(reducer)

    init {
        fetchLeaderboard()
    }

    private fun fetchLeaderboard(){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO){
                    val standings = leaderboardRepository.getStandings()
                    val mapa = mutableMapOf<String, Int>()
                    standings.forEach{
                        mapa.compute(it.nickname) { _, v -> (v ?: 0) + 1 }
                    }
                    setState { copy(data = standings, gamesPlayed = mapa) }
                }
            }catch (error: Exception){
                setState { copy(error = GlobalLeaderboardState.LeaderboardError.DataFetchFailed(error)) }
            }finally {
                setState { copy(fetching = false) }
            }
        }
    }
}