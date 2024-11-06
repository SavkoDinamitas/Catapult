package salko.dinamitas.catalist.quiz.states


interface QuizEvents {
    data class nextQuestion(val left : Int) : QuizEvents
    data object satartQuiz : QuizEvents
}
