package salko.dinamitas.catalist.breeds.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import salko.dinamitas.catalist.breeds.api.model.Image
import salko.dinamitas.catalist.breeds.api.model.Weight
import salko.dinamitas.catalist.networking.serialization.BraindeadBoolSerializer

@Entity
data class Breed(
    @PrimaryKey
    val id : String,
    val name : String,
    val description : String,
    val temperamentList : String,
    val alternateNamesList : String = "",
    @Embedded("image_")
    val image: Image?=null,
    val idSlike: String = "",
    val country: String,
    val life: String,
    @Embedded("weight_")
    val weight: Weight,
    val energy: Int,
    val childFriendly: Int,
    val affection: Int,
    val intelligence: Int,
    val healthIssues: Int,
    val wiki: String = "",
    val rare: Boolean
)
