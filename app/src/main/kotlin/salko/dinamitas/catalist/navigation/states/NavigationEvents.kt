package salko.dinamitas.catalist.navigation.states

import salko.dinamitas.catalist.user.User

sealed class NavigationEvents {
    data class register(val user: User):NavigationEvents()
}