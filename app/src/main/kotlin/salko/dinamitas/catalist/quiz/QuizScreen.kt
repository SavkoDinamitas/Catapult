package salko.dinamitas.catalist.quiz

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import com.example.compose.CatalistTheme
import salko.dinamitas.catalist.R
import salko.dinamitas.catalist.breeds.db.Image
import salko.dinamitas.catalist.breeds.list.states.BreedsListEvents
import salko.dinamitas.catalist.quiz.question.Question
import salko.dinamitas.catalist.quiz.states.QuizEvents
import salko.dinamitas.catalist.quiz.states.QuizState

fun NavGraphBuilder.quizScreen(
    route:String,
    navController: NavController,
) = composable(route = route){

    Scaffold {paddingValues ->
        val quizViewModel = hiltViewModel<QuizViewModel>()
        val state by quizViewModel.state.collectAsState()

        LaunchedEffect(quizViewModel) {
            quizViewModel.effect.collect {
                when (it) {
                    is QuizState.SideEffect.QuizEnded -> quizEnded(navController, it.finalResult)
                }
            }
        }

        var showExitDialog by remember { mutableStateOf(false) }

        if(!state.started){
            BackHandler {
                navController.navigate(route = "breeds")
            }
            QuizPage(title = "Catapult Quiz",
                description = "Test your knowledge with our exciting quiz. Are you ready to start?",
                image = painterResource(id = R.drawable.mackica2),
                eventPublisher = {quizViewModel.setEvent(it)},
                modifier = Modifier.padding(paddingValues),
                buttonLabel = "Start Quiz"
            )
        }
        else{
            BackHandler {
                showExitDialog = true
            }
            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    title = { Text(text = "Exit Quiz") },
                    text = { Text(text = "Are you sure you want to exit the quiz?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showExitDialog = false
                                navController.navigate("breeds"){
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showExitDialog = false }
                        ) {
                            Text("No")
                        }
                    }
                )
            }
            if(state.questions.size > state.currentQuestion){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp).padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "Time left: " + state.timer,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    AnimatedContent(
                        targetState = state.currentQuestion,
                        label = "Question",
                        transitionSpec = {
                            slideInHorizontally(initialOffsetX = { it }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { -2 * it})
                        }
                    ) { q ->
                        QuizQuestionPage(
                            question = state.questions[q],
                            answer1 = state.questions[q].leftPicture,
                            answer2 = state.questions[q].rightPicture,
                            eventPublisher = {quizViewModel.setEvent(it)}
                        )
                    }
                }
            }
            else{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}

@Composable
fun QuizPage(
    title: String,
    description: String,
    image: Painter,
    eventPublisher: (QuizEvents) -> Unit,
    modifier: Modifier = Modifier,
    buttonLabel: String
) {
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
            Button(
                onClick = {
                    eventPublisher(QuizEvents.satartQuiz)
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

}

@Composable
fun QuizQuestionPage(
    question: Question,
    answer1: Image,
    answer2: Image,
    eventPublisher: (QuizEvents) -> Unit
) {
    var alpha by remember {
        mutableFloatStateOf(0f)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = question.question,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .clickable(onClick = { alpha = 0.5f; eventPublisher(QuizEvents.nextQuestion(1)) })
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(model = answer1.url,
                    contentDescription = answer1.breedId,
                    loading = { CircularProgressIndicator() },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .width(500.dp)
                        .height(200.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            (if (!question.leftTrue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary).copy(
                                alpha = alpha
                            )
                        )
                ) {}
            }
            Box(
                modifier = Modifier
                    .clickable(onClick = { alpha = 0.5f; eventPublisher(QuizEvents.nextQuestion(2)) })
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(model = answer2.url, contentDescription = answer2.breedId, loading = {CircularProgressIndicator()}, modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .width(500.dp)
                    .height(200.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            (if (question.leftTrue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary).copy(
                                alpha = alpha
                            )
                        )
                ) {}
            }
        }
    }
}

fun quizEnded(navController: NavController, finalResult: Float){
    Log.d("usro", "Usao sam ovde")
    navController.navigate(route = "quizEnd/$finalResult")
}

@Preview(showBackground = true)
@Composable
fun QuizStartingPagePreview() {
    CatalistTheme {
        QuizPage(
            title = "Welcome to the Quiz!",
            description = "Test your knowledge with our exciting quiz. Are you ready to start?",
            image = painterResource(id = R.drawable.mackica), // Replace with your image resource
            buttonLabel = "Start quiz",
            eventPublisher = {

            }
        )
    }
}