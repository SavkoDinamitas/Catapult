package salko.dinamitas.catalist.breeds.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import salko.dinamitas.catalist.networking.serialization.BraindeadBoolSerializer

@Serializable()
data class BreedApiModel(
    val id : String,
    val name : String,
    val description : String,
    @SerialName("temperament")
    val temperamentList : String,
    @SerialName("alt_names")
    val alternateNamesList : String = "",
    val image: Image?=null,
    @SerialName("reference_image_id")
    val idSlike: String = "",
    @SerialName("origin")
    val country: String,
    @SerialName("life_span")
    val life: String,
    val weight: Weight,
    @SerialName("energy_level")
    val energy: Int,
    @SerialName("child_friendly")
    val childFriendly: Int,
    @SerialName("affection_level")
    val affection: Int,
    val intelligence: Int,
    @SerialName("health_issues")
    val healthIssues: Int,
    @SerialName("wikipedia_url")
    val wiki: String = "",
    @Serializable(with = BraindeadBoolSerializer::class)
    val rare: Boolean
)
@Serializable
data class Image(
    val id: String,
    val width: Int,
    val height: Int,
    val url: String
    )
@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)