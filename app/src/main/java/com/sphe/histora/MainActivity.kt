package com.sphe.histora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.sphe.histora.ui.theme.HistoraTheme
import org.json.JSONArray


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HistoraTheme {
                MainApp()
            }
        }
    }
}

data class QA(
    val question: String,
    val answer: Boolean,
    val explanation: String
)

val apiKey = "AIzaSyAUWCImx47c6tnmuZHNJCT0Sk8c16jNNlw"

@Composable
fun QuestionsUI(
    qaItems: List<QA>,
    questionIndex: Int,
    answers: List<Boolean>,
    onAnswer: (Boolean) -> Unit,
    onRestart: () -> Unit = {}
) {

    //UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (questionIndex < qaItems.size) {
            val item = qaItems[questionIndex]

            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Question ${questionIndex + 1} of ${qaItems.size}", fontSize = 13.sp)

                Spacer(modifier = Modifier.height(20.dp))

                LinearProgressIndicator(
                    color = Color(128, 232, 136),
                    trackColor = Color(242, 243, 245),
                    progress = { (questionIndex + 1).toFloat() / qaItems.size.toFloat() },
                )

                Spacer(modifier = Modifier.height(30.dp))

                Column (
                    modifier = Modifier
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(128, 232, 136))
                        .padding(30.dp)
                ){
                    Text("True or False?")
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        fontSize = 20.sp,
                        text = item.question,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row (
                    modifier = Modifier
                        .padding(top = 20.dp, start = 25.dp, end = 25.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onAnswer(false)
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, Color(228, 230, 235)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text("False", fontSize = 18.sp,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedButton(
                        onClick = {
                            onAnswer(true)
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, Color(228, 230, 235)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text("True", fontSize = 18.sp,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                        )
                    }
                }
            }
        } else if (qaItems.isEmpty()) {
            // Loading UI
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Generating questions...", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(color = Color.Black)
            }
        } else if (questionIndex >= qaItems.size) {
            val score = qaItems.zip(answers).count { (qa, userAnswer) ->
                qa.answer == userAnswer
            }
            val scorePercent = (score.toFloat() / qaItems.size * 100).toInt()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text("Quiz Results", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$scorePercent%", fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "You got $score out of ${qaItems.size} correct",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                items(qaItems.size) { index ->
                    val qa = qaItems[index]
                    val userAnswer = answers[index]
                    val isCorrect = userAnswer == qa.answer
                    val bgColor = if (isCorrect) Color(0xFFE6F4EA) else Color(0xFFFDEAEA)
                    val icon = if (isCorrect) "✅" else "❌"
                    val iconColor = if (isCorrect) Color(0xFF22C55E) else Color(0xFFEF4444)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .clip(RoundedCornerShape(10.dp))
                            .border(BorderStroke(1.dp, Color(0xFFE5E7EB)))
                    ) {
                        Row(
                            modifier = Modifier
                                .background(bgColor)
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = icon,
                                fontSize = 18.sp,
                                color = iconColor,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Column {
                                Text(qa.question, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Your answer: ${if (userAnswer) "True" else "False"}", fontSize = 14.sp)
                                Text("Correct answer: ${if (qa.answer) "True" else "False"}", fontSize = 14.sp)
                            }
                        }

                        Text(
                            text = "Explanation: ${qa.explanation}",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .background(Color(0xFFF9FAFB))
                                .padding(12.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onRestart,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Restart Quiz", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    var qaItems by remember { mutableStateOf<List<QA>>(emptyList()) }
    var reloadTrigger by remember { mutableStateOf(false) }
    var questionIndex by remember { mutableStateOf(0) }
    val answers = remember { mutableStateListOf<Boolean>() }

    var screenState by remember { mutableStateOf("welcome") } // "welcome", "quiz", "score"

    //Launch once on build
    LaunchedEffect(reloadTrigger) {
        qaItems = emptyList()
        questionIndex = 0
        answers.clear()

        val generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = apiKey
        )
        val prompt =
            "Give me 5 unique historical true-or-false questions with increasing difficulty. Each must include a \"question\" (string), an \"answer\" (boolean), and an \"explanation\" (string). Return strictly as a JSON array. No extra text. No \"True or False\" text before the question."
        val response = generativeModel.generateContent(prompt)

        // remove ```json & ```
        val responseString: String = response.text.toString()
        val cleanedResponse = responseString
            .replace("```json", "")
            .replace("```", "")

        //convert json to map
        val jsonArray = JSONArray(cleanedResponse)
        val qaList = (0 until jsonArray.length()).map {
            val obj = jsonArray.getJSONObject(it)
            QA(
                obj.getString("question"),
                obj.getBoolean("answer"),
                obj.getString("explanation"),
            )
        }
        qaItems = qaList
    }
    // Pass questions to the QuestionsUI and have screens
    when (screenState) {
        "welcome" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9FAFB))
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Histora",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Welcome! Test your history knowledge\nwith fun True or False questions.",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    fontStyle = FontStyle.Italic
                )

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = { screenState = "quiz" },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Start", fontSize = 18.sp)
                }
            }
        }

        "quiz" -> {
            QuestionsUI(
                qaItems = qaItems,
                questionIndex = questionIndex,
                answers = answers,
                onAnswer = { answer ->
                    answers.add(answer)
                    if (questionIndex + 1 == qaItems.size) {
                        screenState = "score"
                    } else {
                        questionIndex++
                    }
                },
                onRestart = {
                    reloadTrigger = !reloadTrigger
                    screenState = "quiz"
                }
            )
        }

        "score" -> {
            QuestionsUI(
                qaItems = qaItems,
                questionIndex = qaItems.size,
                answers = answers,
                onAnswer = {},
                onRestart = {
                    reloadTrigger = !reloadTrigger
                    screenState = "quiz"
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HistoraTheme {
        val dummyData = listOf(
            QA("Was Julius Caesar Roman?", true, "Julius Caesar was a Roman general and statesman central to the fall of the Roman Republic."),
            QA("Did WW2 start in 1935?", false, "World War II officially began on September 1, 1939, when Germany invaded Poland.")
        )

        val questionIndex = 0
        val answers = listOf<Boolean>()

        HistoraTheme {
            QuestionsUI(
                qaItems = dummyData,
                questionIndex = questionIndex,
                answers = answers,
                onAnswer = {},
                onRestart = {}
            )
        }
    }
}