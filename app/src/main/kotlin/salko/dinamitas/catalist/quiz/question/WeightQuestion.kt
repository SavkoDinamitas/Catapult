package salko.dinamitas.catalist.quiz.question

import salko.dinamitas.catalist.breeds.db.Image

data class WeightQuestion(
    override val leftPicture: Image,
    override val rightPicture: Image,
    override val rightAvg: Float,
    override val leftAvg: Float,
    override val leftTrue: Boolean
):Question{
    override val question = "Which cat is heavier?"
}