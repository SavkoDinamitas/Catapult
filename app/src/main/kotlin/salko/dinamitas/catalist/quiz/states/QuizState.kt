package salko.dinamitas.catalist.quiz.states

import salko.dinamitas.catalist.breeds.api.model.BreedApiModel
import salko.dinamitas.catalist.quiz.question.Question

data class QuizState(
    val breeds: List<BreedApiModel>? = null,
    val questions: List<Question> = listOf(),
    val fetching: Boolean = false,
    val error: QuizError? = null,
    val points: Float = 0f,
    val timer: Int = 300,
    val started: Boolean = false,
    val currentQuestion: Int = 0
){
    sealed class QuizError {
        data class QuizPreparationFailed(val cause: Throwable? = null) : QuizError()
    }
    sealed class SideEffect {
        data class QuizEnded(val finalResult: Float) : SideEffect()
    }
}