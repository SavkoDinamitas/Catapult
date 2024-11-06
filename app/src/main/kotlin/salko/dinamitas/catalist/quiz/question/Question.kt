package salko.dinamitas.catalist.quiz.question

import salko.dinamitas.catalist.breeds.db.Image

interface Question{
    val question: String
    val leftPicture: Image
    val rightPicture: Image
    val leftAvg: Float
    val rightAvg: Float
    val leftTrue: Boolean
}