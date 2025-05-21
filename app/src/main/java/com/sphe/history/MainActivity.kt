package com.sphe.history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sphe.history.ui.theme.HistoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HistoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // Referencing: ChatGPT prompt and Q&A result - https://chatgpt.com/share/682cfc15-3d80-800e-9699-0715c738b916
    // I did not rephrase any of the questions I received from ChatGPT, as I believe I'm not being tested on my
    // ability to construct history questions, but rather on creating functional code.

    // Declarations
    val questions = arrayOf("Hitler was born in Austria.", "The Great Wall of China can be seen from the Moon with the naked eye.", "Nelson Mandela became South Africa's first black president in 1994.", "The Roman Empire fell in 476 AD.", "The United States declared independence in 1776.")
    val answers = arrayOf("True", "False", "True", "True", "True")
    var result by remember { mutableStateOf(listOf<String>())}
    var i by remember { mutableIntStateOf(0) }
    var welcome by remember { mutableStateOf(true) }

    Column (
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (welcome) {
            Text(
                text = "History",
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
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = { welcome = false },
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
        // Loop through all questions
        if (i < questions.size && !welcome) {
            Text(text = "Question ${i + 1} of ${questions.size}", fontSize = 13.sp)

            Spacer(modifier = Modifier.height(20.dp))

            LinearProgressIndicator(
                color = Color(128, 232, 136),
                trackColor = Color(242, 243, 245),
                progress = { (i + 1).toFloat() / questions.size.toFloat() },
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(128, 232, 136))
                    .padding(30.dp)
            ) {
                Text("True or False?")
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    fontSize = 20.sp,
                    text = questions[i],
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 20.dp, start = 25.dp, end = 25.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        // Track correct and incorrect answers
                        result += if ("True" == answers[i]) {
                            "Correct"
                        } else {
                            "Incorrect"
                        }
                        // Move to next question
                        i++
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(2.dp, Color(228, 230, 235)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        "True", fontSize = 18.sp,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedButton(
                    onClick = {
                        // Track correct and incorrect answers
                        result += if ("False" == answers[i]) {
                            "Correct"
                        } else {
                            "Incorrect"
                        }
                        // Move to next question
                        i++
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(2.dp, Color(228, 230, 235)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        "False", fontSize = 18.sp,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                    )
                }
            }
        } else if (!welcome) {
            // Calculate Score
            var correct = result.count { it == "Correct" }.toDouble()
            var total = answers.size
            var score = correct / total * 100
            Text("Quiz Results")
            Text("$score%")
            Text("$correct correct out of $total questions")

            for (i in result.indices) {
                Row {
                    if (result[i] == "Correct") "✅" else "❌"
                    Column {
                        Text("Q${i + 1}")
                        Text("Question: ${questions[i]}")
                        Text("Answer: ${answers[i]}")
//                        Text("Result: ${result[i]}")
                    }
                }
            }

            // Replay
            Button(
                onClick = {
                    result = listOf()
                    i = 0
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Replay", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HistoryTheme {
        Greeting("Android")
    }
}