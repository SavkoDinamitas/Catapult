package salko.dinamitas.catalist.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import salko.dinamitas.catalist.breeds.api.model.BreedApiModel
import salko.dinamitas.catalist.breeds.list.states.BreedsListEvents
import salko.dinamitas.catalist.breeds.repository.BreedRepository
import salko.dinamitas.catalist.mappers.asBreedApiModel
import salko.dinamitas.catalist.quiz.question.LifeQuestion
import salko.dinamitas.catalist.quiz.question.WeightQuestion
import salko.dinamitas.catalist.quiz.states.QuizEvents
import salko.dinamitas.catalist.quiz.states.QuizState
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val breedRepository: BreedRepository
): ViewModel(){
    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()
    private fun setState(reducer: QuizState.() -> QuizState) = _state.getAndUpdate(reducer)

    private val _effect: Channel<QuizState.SideEffect> = Channel()
    val effect = _effect.receiveAsFlow()
    private fun setEffect(effect: QuizState.SideEffect) =
        viewModelScope.launch {
            _effect.send(effect)
        }

    private val events = MutableSharedFlow<QuizEvents>()


    fun setEvent(event: QuizEvents) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        fetchBreeds()
        observeBreeds()
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is QuizEvents.satartQuiz -> {
                        startQuiz()
                    }
                    is QuizEvents.nextQuestion -> {
                        nextQuestion(left = it.left)
                    }
                }
            }
        }
    }

    private fun nextQuestion(left: Int){
        viewModelScope.launch {
            delay(300)
            val l = left == 1
            if(l == state.value.questions[state.value.currentQuestion].leftTrue){
                val points = state.value.points + 1
                setState { copy(points = points) }
            }
            val question = state.value.currentQuestion + 1
            setState { copy(currentQuestion = question) }
            if(question >= 20)
                endQuiz()
        }
    }

    private fun startQuiz(){
        viewModelScope.launch {
            setState { copy(started = true) }
            while (true){
                delay(1000)
                var time = state.value.timer - 1
                if(time < 0)
                    break
                setState { copy(timer = timer-1) }
            }
            endQuiz()
        }
    }

    private fun endQuiz(){
        Log.d("usro", "Dosao sam ovde")
        val finalPoints =
            (state.value.points * 2.5f * (1f + (state.value.timer + 120f) / 300f)).coerceAtMost(maximumValue = 100.00f)
        setEffect(QuizState.SideEffect.QuizEnded(finalResult = finalPoints))
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    breedRepository.getAllBreeds()
                    val breeds = state.value.breeds
                    if(breeds != null)
                        generateQuestions(breeds)
                }
            } catch (error: Exception) {
                Log.d("RMA", "Exception", error)
            }finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            breedRepository.observeBreeds()
                .distinctUntilChanged()
                .collect {
                    setState { copy(breeds = it.map { it.asBreedApiModel() }) }
                }
        }
    }

    private suspend fun generateWeightQuestion(breedsList: List<BreedApiModel>, usedBreeds: MutableSet<BreedApiModel>){
        while(true){
            val breed1 = breedsList.random()
            val breed2 = breedsList.random()
            val b1w = (breed1.weight.imperial.split(" - ")[0].toFloat() + breed1.weight.imperial.split(" - ")[1].toFloat()) / 2f
            val b2w = (breed2.weight.imperial.split(" - ")[0].toFloat() + breed2.weight.imperial.split(" - ")[1].toFloat()) / 2f
            val images1 = breedRepository.getBreedImages(breed1.id)
            val images2 = breedRepository.getBreedImages(breed2.id)
            if(b1w == b2w || usedBreeds.contains(breed1) || usedBreeds.contains(breed2) || images1.images.isEmpty() || images2.images.isEmpty())
                continue
            usedBreeds.add(breed1)
            usedBreeds.add(breed2)
            val question = WeightQuestion(leftPicture = images1.images.random(), rightPicture = images2.images.random(), leftAvg = b1w, rightAvg = b2w, leftTrue = b1w>b2w)
            Log.d("kineska komedija", question.toString())
            val xd = state.value.questions + question
            setState { copy(questions = xd) }
            return
        }
    }

    private suspend fun generateLifeQuestion(breedsList: List<BreedApiModel>, usedBreeds: MutableSet<BreedApiModel>){
        while(true){
            val breed1 = breedsList.random()
            val breed2 = breedsList.random()
            val b1w = (breed1.life.split(" - ")[0].toFloat() + breed1.life.split(" - ")[1].toFloat()) / 2f
            val b2w = (breed2.life.split(" - ")[0].toFloat() + breed2.life.split(" - ")[1].toFloat()) / 2f
            val images1 = breedRepository.getBreedImages(breed1.id)
            val images2 = breedRepository.getBreedImages(breed2.id)
            if(b1w == b2w || usedBreeds.contains(breed1) || usedBreeds.contains(breed2) || images1.images.isEmpty() || images2.images.isEmpty())
                continue
            usedBreeds.add(breed1)
            usedBreeds.add(breed2)
            val question = LifeQuestion(leftPicture = images1.images.random(), rightPicture = images2.images.random(), leftAvg = b1w, rightAvg = b2w, leftTrue = b1w>b2w)
            Log.d("kineska komedija", question.toString())
            val xd = state.value.questions + question
            setState { copy(questions = xd) }
            return
        }
    }

    private suspend fun generateQuestions(breedsList: List<BreedApiModel>){
        val usedBreeds = mutableSetOf<BreedApiModel>()
        val xd = listOf(0, 1)
        repeat(20){
            if(xd.random() > 0){
                generateLifeQuestion(breedsList, usedBreeds)
            }
            else{
                generateWeightQuestion(breedsList, usedBreeds)
            }
        }
    }

}