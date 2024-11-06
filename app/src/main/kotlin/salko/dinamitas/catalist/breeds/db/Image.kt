package salko.dinamitas.catalist.breeds.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Image (
    @PrimaryKey
    val id: String,
    val width: Int,
    val height: Int,
    val url: String,
    val breedId: String = ""
)