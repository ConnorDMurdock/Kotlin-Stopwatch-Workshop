package com.example.timetrackingappcompleted

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrackingappcompleted.ui.theme.TimeTrackingAppCompletedTheme
import kotlinx.coroutines.delay

class StopwatchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTrackingAppCompletedTheme {
                Stopwatch(modifier = Modifier, this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Stopwatch(modifier: Modifier = Modifier, context: Context) {
    //mutableStateOf is required to be able to update values to be displayed
    //The way compose works is that it generates the UI only once, then when a display update is called (such as turning the screen)
    //It regenerates the entire UI. using mutableStateOf by remember allows these values to be updated without needing to regenerate the UI every time
    var isRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var comment by remember { mutableStateOf("") }

    //This is a coroutine that will add 1000 milliseconds to the timer, then wait 1000 milliseconds, effectively incrementing it by 1 second every second
    LaunchedEffect(isRunning) {
        while (isRunning) {
            elapsedTime += 1000L
            delay(1000L)
        }
    }

    //Using a column here means that each element will be placed under each other vertically
    Column (
        modifier = modifier.fillMaxSize().wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = formatTime(elapsedTime),
            fontWeight = FontWeight.Bold,
            fontSize = 80.sp)

        //Using a row here means that all of the buttons will be on the same row, instead of underneath each other
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            //This button is to start and stop the timer
            Button(onClick = { isRunning = !isRunning }) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Close else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(if (isRunning) "Pause" else "Start")
            }

            //This button resets the timer
            Button(onClick = {
                isRunning = false
                elapsedTime = 0
            }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text("Reset")
            }

            //This button saves the current time. If the timer is running, it'll pause it before saving
            Button(onClick = {
                if (isRunning) {
                    isRunning = !isRunning
                }
                val bundle = Bundle()
                //using a $ here is string interpolation
                //another example of string interpolation are python using f-strings
                val savedTime = arrayOf(comment, "$elapsedTime")
                bundle.putStringArray("savedTime", savedTime)
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text("Save")
            }
        }
        //This row is for the text input so the user can add a comment to their saved time
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comment") }
            )
        }
    }
}

//This formats the time from milliseconds to 00:00 as we'd expect from a timer
@Composable
fun formatTime(timeMilli: Long): String {
    val seconds = (timeMilli/1000).toInt()
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    //This format string is a little complicated. Here's the rundown:
    //The % means it's a formatted string
    //The 0 means to pad with 0's
    //the 2 means to use at least 2 characters
    //the d means it's an integer
    //For example, if it's given minutes = 5 and remaining seconds = 7,
    //it'll return 05:07
    return "%02d:%02d".format(minutes, remainingSeconds)
}

