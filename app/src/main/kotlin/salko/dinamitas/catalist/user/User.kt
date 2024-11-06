package salko.dinamitas.catalist.user

import kotlinx.serialization.Serializable

@Serializable
data class CatalistConfig(
    val user: User?
)
@Serializable
data class User(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
