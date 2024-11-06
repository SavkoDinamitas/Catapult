package salko.dinamitas.catalist.breeds.db

import androidx.room.Embedded
import androidx.room.Relation
import salko.dinamitas.catalist.breeds.db.Breed
import salko.dinamitas.catalist.breeds.db.Image

data class ImagesForBreed(
    @Embedded val user: Breed,
    @Relation(
        parentColumn = "id",
        entityColumn = "breedId"
    )
    val images: List<Image>
)
