package salko.dinamitas.catalist.login

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import salko.dinamitas.catalist.details.gallery.BreedGalleryScreen
import salko.dinamitas.catalist.details.gallery.BreedGalleryViewModel
import salko.dinamitas.catalist.navigation.NavigationViewModel
import salko.dinamitas.catalist.navigation.states.NavigationEvents
import salko.dinamitas.catalist.user.CatalistConfig
import salko.dinamitas.catalist.user.User


fun NavGraphBuilder.loginScreen(
    route:String,
    navController: NavController,
) = composable(route = route){

    Scaffold {
        paddingValues ->
        val navigationViewModel = hiltViewModel<NavigationViewModel>()
        RegistrationForm({navigationViewModel.setEvent(it)}, Modifier.padding(paddingValues))
    }

}
@Composable
fun RegistrationForm(
    eventPublisher: (NavigationEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Catapult",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = firstName.isBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = lastName.isBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            isError = !username.matches("^[a-zA-Z0-9_]+$".toRegex())
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if(username.matches("^[a-zA-Z0-9_]+$".toRegex()) && lastName.isNotEmpty() && firstName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    eventPublisher(NavigationEvents.register(User(firstName = firstName, lastName = lastName, username = username, email = email)))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Register")
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun RegistrationFormPreview() {
    CatalistTheme {
        RegistrationForm()
    }
}*/