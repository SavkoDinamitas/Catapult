package salko.dinamitas.catalist.details.gallery

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import coil.compose.SubcomposeAsyncImage
import com.example.compose.CatalistTheme


fun NavGraphBuilder.breedGalleryScreen(
    route:String,
    navController: NavController,
) = composable(route = route){
        navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("breedId")
        ?: throw IllegalArgumentException("BreedId is required.")

    val breedGalleryViewModel = hiltViewModel<BreedGalleryViewModel>()
    val state by breedGalleryViewModel.state.collectAsState()
    BreedGalleryScreen(state = state)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BreedGalleryScreen(
    state : BreedGalleryState,
){
    Log.d("mast", "$state")
    //Log.d("mast", "$page")
    val pagerState = rememberPagerState(pageCount = { state.data?.size ?: 0 })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)
    ) { page ->
            SubcomposeAsyncImage(model = state.data?.get(page)?.url,
                contentDescription = state.breedId,
                loading = { CircularProgressIndicator() },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
    }
}

@Preview
@Composable
fun PreviewPasswordListScreen() {
    CatalistTheme {
        BreedGalleryScreen(
            state = BreedGalleryState("aby")
        )
    }
}