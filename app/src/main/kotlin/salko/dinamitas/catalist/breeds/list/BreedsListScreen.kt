package salko.dinamitas.catalist.breeds.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.compose.CatalistTheme
import salko.dinamitas.catalist.breeds.api.model.BreedApiModel
import salko.dinamitas.catalist.breeds.list.states.BreedsListEvents
import salko.dinamitas.catalist.breeds.list.states.BreedsListState

fun NavGraphBuilder.breedsListScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val breedsListViewModel = hiltViewModel<BreedsListViewModel>()
    val state by breedsListViewModel.state.collectAsState()

    BreedsListScreen(
        state = state,
        onItemClick = { navController.navigate(route = "breeds/${it.id}") },
        eventPublisher = { breedsListViewModel.setEvent(it) })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedsListScreen(
    state: BreedsListState,
    onItemClick: (BreedApiModel) -> Unit,
    eventPublisher: (BreedsListEvents) -> Unit
) {
    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "BreedList") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },
        content = {
            var kineskaKomedija by remember { mutableStateOf("") }
            Column(modifier = Modifier.padding(it)) {

                SearchBar(query = kineskaKomedija,
                    onQueryChange = {
                        kineskaKomedija = it
                        eventPublisher(BreedsListEvents.BreedSearch(kineskaKomedija))
                    },
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    placeholder = { Text(text = "Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null
                        )
                    })
                {}
                if (kineskaKomedija.isNotEmpty()) {
                    BreedsList(
                        lista = state.filtered,
                        onItemClick = onItemClick,
                    )
                } else {
                    BreedsList(
                        //paddingValues = it,
                        lista = state.breeds,
                        onItemClick = onItemClick,
                    )
                }
            }




            if (state.breeds.isEmpty()) {
                when (state.fetching) {
                    true -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    false -> {
                        if (state.error != null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                val errorMessage = when (state.error) {
                                    is BreedsListState.ListError.ListUpdateFailed ->
                                        "Failed to load. Error message: ${state.error.cause?.message}."
                                }
                                Text(text = errorMessage)
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = "No passwords.")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun BreedsList(
    lista: List<BreedApiModel>,
    onItemClick: (BreedApiModel) -> Unit
) {
    val scrollState = rememberScrollState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        items(lista) {
            key(it.id) {
                BreedListItem(
                    data = it,
                    onClick = {
                        onItemClick(it)
                    },
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }


    }
}

@Composable
private fun BreedListItem(
    data: BreedApiModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = data.name,
            style = MaterialTheme.typography.titleLarge,
        )
        if (data.alternateNamesList.isNotEmpty()) {
            Text(
                text = "alternate names: ${data.alternateNamesList}",
                Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }

        val komedija =
            if (data.description.length > 250) data.description.take(250) + "..." else data.description
        Row {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .weight(weight = 1f),
                text = komedija,
            )

            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
            )
        }

        val majmunisanje = data.temperamentList.split(", ").take(3)
        Row {
            majmunisanje.forEach() {
                FilterChip(
                    selected = true,
                    onClick = { /*TODO*/ },
                    label = { Text(text = it) },
                    Modifier.padding(10.dp)
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewPasswordListScreen() {
    CatalistTheme {
        BreedsListScreen(
            state = BreedsListState(breeds = SampleData),
            onItemClick = {},
            eventPublisher = {}
        )
    }
}