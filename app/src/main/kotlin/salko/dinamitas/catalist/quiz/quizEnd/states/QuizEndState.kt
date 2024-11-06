package salko.dinamitas.catalist.quiz.quizEnd.states

data class QuizEndState(
    val finalScore: Float = -1f,
    val fetching: Boolean = false,
    val error: PublishResError? = null,
    val published: Boolean = false
){
    sealed class PublishResError {
        data class DataPublishFailed(val cause: Throwable? = null) : PublishResError()
    }
}
