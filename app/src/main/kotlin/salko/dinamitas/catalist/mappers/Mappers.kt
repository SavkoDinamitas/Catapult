package salko.dinamitas.catalist.mappers

import androidx.room.Embedded
import salko.dinamitas.catalist.breeds.api.model.BreedApiModel
import salko.dinamitas.catalist.breeds.api.model.Image
import salko.dinamitas.catalist.breeds.api.model.Weight
import salko.dinamitas.catalist.breeds.db.Breed

fun BreedApiModel.asBreedDbModel(): Breed {
    return Breed(
        id = this.id,
        name = this.name,
        description = this.description,
        temperamentList = this.temperamentList,
        alternateNamesList = this.alternateNamesList,
        image = this.image,
        idSlike = this.idSlike,
        country = this.country,
        life = this.life,
        weight = this.weight,
        energy = this.energy,
        childFriendly = this.childFriendly,
        affection = this.affection,
        intelligence = this.intelligence,
        healthIssues = this.healthIssues,
        wiki = this.wiki,
        rare = this.rare
    )
}

fun Breed.asBreedApiModel(): BreedApiModel{
    return BreedApiModel(
        id = this.id,
        name = this.name,
        description = this.description,
        temperamentList = this.temperamentList,
        alternateNamesList = this.alternateNamesList,
        image = this.image,
        idSlike = this.idSlike,
        country = this.country,
        life = this.life,
        weight = this.weight,
        energy = this.energy,
        childFriendly = this.childFriendly,
        affection = this.affection,
        intelligence = this.intelligence,
        healthIssues = this.healthIssues,
        wiki = this.wiki,
        rare = this.rare
    )
}