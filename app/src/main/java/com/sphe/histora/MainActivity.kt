package com.sphe.histora

import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    val answer: Boolean
)

@Composable
fun QuestionsUI(qaItems: List<QA>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // show one question at a time, with a button to show the next question
        var questionIndex by remember { mutableStateOf(0) }

        if (questionIndex < qaItems.size) {
            val item = qaItems[questionIndex]

            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Question ${questionIndex + 1} of ${qaItems.size}", fontSize = 18.sp)

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    color = Color(128, 232, 136),
                    trackColor = Color(242, 243, 245),
                    progress = { (questionIndex + 1).toFloat() / qaItems.size.toFloat() },
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column (
                    modifier = Modifier
                        .height(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(128, 232, 136))
                        .padding(20.dp)
                ){
                    Text("True or False?")
                    Text(
                        fontSize = 20.sp,
                        text = item.question,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Button(onClick = { questionIndex++ }) {
                        Text("False")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = { questionIndex++ }) {
                        Text("True")
                    }
                }
            }
        } else {
            Text("All questions completed.")
        }
    }
}

@Composable
fun MainApp() {
    var qaItems by remember {
        mutableStateOf(listOf<QA>())
    }

    //Launch once on build
    LaunchedEffect(Unit) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = "AIzaSyAUWCImx47c6tnmuZHNJCT0Sk8c16jNNlw"
        )
        val prompt =
            "Give me and unique five historical true-or-false questions in increasing difficulty. Give new questions every time. Return the output strictly as a JSON array with each object containing a \"question\" and an \"answer\" field. \"answer\" must be a boolean (true or false). Do not include any explanation or extra text. And don't include"
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
            QA(obj.getString("question"), obj.getBoolean("answer"))
        }
        qaItems = qaList
    }
    // Pass questions to the QuestionsUI
    QuestionsUI(qaItems = qaItems)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HistoraTheme {
        val dummyData = listOf(
            QA("Was Julius Caesar Roman?", true),
            QA("Did WW2 start in 1935?", false)
        )
        HistoraTheme {
            QuestionsUI(qaItems = dummyData)
        }
    }
}