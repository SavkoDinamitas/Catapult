package salko.dinamitas.catalist.quiz.quizEnd.states

interface QuizEndEvents {
    data class publishResults(val finalResult: Float):QuizEndEvents
}