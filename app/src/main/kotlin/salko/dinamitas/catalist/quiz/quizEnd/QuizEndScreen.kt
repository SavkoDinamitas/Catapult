package salko.dinamitas.catalist.quiz.quizEnd

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import salko.dinamitas.catalist.R
import salko.dinamitas.catalist.quiz.quizEnd.states.QuizEndEvents
import salko.dinamitas.catalist.quiz.quizEnd.states.QuizEndState

fun NavGraphBuilder.quizEndScreen(
    route:String,
    navController: NavController,
) = composable(route = route, arguments = listOf(
    navArgument(name = "finalScore") {
        type = NavType.FloatType
    }
)) {

    Scaffold {
        paddingValudes ->
        val quizViewModel = hiltViewModel<QuizEndViewModel>()
        val state by quizViewModel.state.collectAsState()

        BackHandler {
            navController.navigate(route = "breeds"){
                popUpTo(0)
            }
        }

        QuizEndPage(
            title = "Final score: " + String.format("%.2f", state.finalScore) + " points",
            description = "Would you like to publish your result to global leaderboard?",
            image = painterResource(id = R.drawable.mackica2),
            eventPublisher = {quizViewModel.setEvent(it)},
            modifier = Modifier.padding(paddingValudes),
            buttonLabel = "Publish results",
            result = state.finalScore,
            state = state
        )
    }

}

@Composable
fun QuizEndPage(
    title: String,
    description: String,
    image: Painter,
    eventPublisher: (QuizEndEvents) -> Unit,
    modifier: Modifier = Modifier,
    buttonLabel: String,
    result: Float,
    state: QuizEndState
) {
    var published by remember {
        mutableStateOf(state.published)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        if(!published){
            Button(
                onClick = {
                    published = true
                    eventPublisher(QuizEndEvents.publishResults(result))
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = buttonLabel,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        else{
            if(state.error != null){
                Spacer(modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth())
                Text(
                    text = "There was an error in publishing your result!",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            else{
                if(state.fetching){
                    CircularProgressIndicator()
                }
                else{
                    Spacer(modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth())
                    Text(
                        text = "Result successfully published!",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

    }

}