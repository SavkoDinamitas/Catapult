package salko.dinamitas.catalist.details

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import salko.dinamitas.catalist.breeds.api.model.BreedApiModel
import salko.dinamitas.catalist.breeds.api.model.Image
import salko.dinamitas.catalist.breeds.list.SampleData


fun NavGraphBuilder.breedDetailsScreen(
    route:String,
    navController: NavController,
) = composable(route = route){
        navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required.")

    val breedDetailsViewModel = hiltViewModel<BreedDetailViewModel>()
    val state = breedDetailsViewModel.state.collectAsState()
    BreedDetailsScreen(state = state.value, onClose = {navController.navigateUp()}, navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(
    state: BreedDetailState,
    onClose: () -> Unit,
    navController: NavController
){
    Scaffold(
        topBar ={
            LargeTopAppBar(
                title = {
                    Text(text = state.data?.name ?: "Loading")
                        },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.fetching) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is BreedDetailState.DetailsError.DataUpdateFailed ->
                                "Failed to load. Error message: ${state.error.cause?.message}."
                        }
                        Text(text = errorMessage)
                    }
                } else if (state.data != null) {
                    BreedDataColumn(
                        data = state.data,
                        image = state.image,
                        navController = navController
                    )
                } else {
                    NoDataContent(id = state.breedId)
                }
            }
        }
    )
}

//cekicanje izgleda jednog mackica screen-a
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun BreedDataColumn(
    data: BreedApiModel,
    image: Image?,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    Column (modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(scrollState),) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Breed description:",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleMedium)

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = data.description,
            textAlign = TextAlign.Justify
        )
        if(image != null){
                SubcomposeAsyncImage(model = image.url, contentDescription = data.name, loading = {CircularProgressIndicator()}, modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp).
                clickable(onClick = {navController.navigate(route = "gallery/${data.id}")}))
        }
        else{
            Text(
                text = "Nazalost nemamo sliku :(",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 30.dp),
                style = MaterialTheme.typography.titleMedium)
        }

        Text(
            text = "Country of origin:",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge)

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = data.country,
        )

        Text(
            text = "Temperament:",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp),
            style = MaterialTheme.typography.titleLarge)

        val majmunisanje = data.temperamentList.split(", ")
        FlowRow {
            majmunisanje.forEach() {
                FilterChip(
                    selected = true,
                    onClick = { /*TODO*/ },
                    label = { Text(text = it) },
                    Modifier.padding(10.dp)
                )
            }
        }

        Text(
            text = "Life span:",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge)

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = data.life + " years",
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Weight:",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge)

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = data.weight.imperial + " lbs",
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = data.life + " kg",
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Traits:",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge)

        Indicator(value = data.energy, trait = "energy")
        Indicator(value = data.childFriendly, trait = "child_friendly")
        Indicator(value = data.healthIssues, trait = "health_issues")
        Indicator(value = data.intelligence, trait = "intelligence")
        Indicator(value = data.affection, trait = "affection_level")


        if(data.rare){
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Rare species",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp),
                fontSize = TextUnit(25f, TextUnitType.Sp))
        }

        if(data.wiki.isNotEmpty()){
            Spacer(modifier = Modifier.height(20.dp))
            UrlButton(text = "Wiki page", url = data.wiki)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun UrlButton(text: String, url: String) {
    val openUrlLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            openUrlLauncher.launch(intent)
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }
}


@Composable
fun Indicator(value: Int, trait: String){
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .padding(bottom = 10.dp)){
        Text(text = trait,
            fontSize = TextUnit(16f, TextUnitType.Sp),
        )
        Spacer(modifier = Modifier.width(20.dp))
        repeat(value){
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "",
                tint = LocalContentColor.current,
                modifier = Modifier.size(16.dp, 16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

@Composable
fun NoDataContent(
    id: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "There is no data for id '$id'.",
            fontSize = 18.sp,
        )
    }
}

