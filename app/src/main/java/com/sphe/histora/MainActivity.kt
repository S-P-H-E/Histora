package com.sphe.histora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.sphe.histora.ui.theme.HistoraTheme
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONTokener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HistoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class QA(
    val question: String,
    val answer: Boolean
)

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var qaItems by remember {
        mutableStateOf(listOf<QA>())
    }

    //Launch once on build
    LaunchedEffect(Unit){
        val generativeModel = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = "AIzaSyAUWCImx47c6tnmuZHNJCT0Sk8c16jNNlw"
        )
        val prompt = "Give me and unique five historical true-or-false questions in increasing difficulty. Give new questions every time. Return the output strictly as a JSON array with each object containing a \"question\" and an \"answer\" field. \"answer\" must be a boolean (true or false). Do not include any explanation or extra text. And don't include"
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
    Column (
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

            Column {
                Text(text = "Question: ${item.question}")
                Text(text = "Answer: ${if (item.answer) "True" else "False"}")

                Button(onClick = { questionIndex++ }) {
                    Text("Next")
                }
            }
        } else {
            Text("All questions completed.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HistoraTheme {
        Greeting()
    }
}