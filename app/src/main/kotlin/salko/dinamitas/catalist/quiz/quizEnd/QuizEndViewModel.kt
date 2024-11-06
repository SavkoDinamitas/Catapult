package salko.dinamitas.catalist.quiz.quizEnd

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import salko.dinamitas.catalist.leaderboard.model.LeaderboardApiModel
import salko.dinamitas.catalist.leaderboard.db.LeaderboardData
import salko.dinamitas.catalist.leaderboard.repository.LeaderboardRepository
import salko.dinamitas.catalist.quiz.quizEnd.states.QuizEndEvents
import salko.dinamitas.catalist.quiz.quizEnd.states.QuizEndState
import salko.dinamitas.catalist.user.UserDataStore
import javax.inject.Inject

@HiltViewModel
class QuizEndViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
    private val dataStore: UserDataStore,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val finalResult: Float = savedStateHandle["finalScore"]!!
    private val _state = MutableStateFlow(QuizEndState(finalResult))
    val state = _state.asStateFlow()
    private var quizId: Long = -1
    private val events = MutableSharedFlow<QuizEndEvents>()

    private fun setState(reducer: QuizEndState.() -> QuizEndState) = _state.getAndUpdate(reducer)


    fun setEvent(event: QuizEndEvents) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        postResult()
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is QuizEndEvents.publishResults -> {
                        publishResult(it.finalResult)
                    }
                }
            }
        }
    }

    private fun postResult() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    var username = dataStore.config.value.user?.username
                    if (username == null)
                        username = "Guest"
                    val res = LeaderboardData(nickname = username, result = finalResult, category = 3)
                    quizId = leaderboardRepository.saveResult(res)
                    Log.d("database", "Id: $quizId")
                }
            } catch (error: Exception) {
                setState { copy(error = QuizEndState.PublishResError.DataPublishFailed(cause = error)) }
            }
        }
    }

    private fun publishResult(result: Float){
        viewModelScope.launch {
            try {
                setState { copy(fetching=true) }
                withContext(Dispatchers.IO) {
                    var username = dataStore.config.value.user?.username
                    if (username == null)
                        username = "xd"
                    val xd = LeaderboardApiModel(category = 3, nickname = username, result = result)
                    val mast = leaderboardRepository.postResult(xd)
                    leaderboardRepository.updateGlobalRanking(quizId, mast.ranking)
                    setState { copy(published = true) }
                }
            } catch (error: Exception){
                setState {
                    copy(error = QuizEndState.PublishResError.DataPublishFailed(cause = error))
                }
            } finally {
                setState { copy(fetching=false) }
            }
        }
    }
}