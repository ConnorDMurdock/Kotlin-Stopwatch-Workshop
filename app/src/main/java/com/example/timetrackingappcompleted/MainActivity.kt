package com.example.timetrackingappcompleted

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrackingappcompleted.ui.theme.TimeTrackingAppCompletedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTrackingAppCompletedTheme {
                Surface(
                    //modifier is a keyword for composable functions, and using "modifier = Modifier" is basically creating a new
                    //modifier with the given parameters and assigning it immediately
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Get the passed value from the bundle
                    val bundle = intent.extras
                    Column {
                        //Context is essential to the android framework and is required to access resources, launch activities, access services, and more.
                        //Activity inherits from Context, so using it this way is basically telling the button "go from here, to there"
                        ChangeActivityButton(context = LocalContext.current, activity = StopwatchActivity::class.java)
                        if (bundle != null) {
                            ListOfTimes(bundle)
                        }
                    }
                }
            }
        }
    }
}

/* Single Activity vs Multiple Activity
 * benefits of single activity:
 * reduced memory usage
 * improved navigation: longer to initially setup, easier to build from
 * customizable animations between screen transitions
 * easier to share data
 *
 * Downsides of single activity:
 * long initial setup
 * can become more complex if handled improperly
 */

@Composable
fun ChangeActivityButton(context: Context, activity: Class<out ComponentActivity>) {
    Button(
        onClick = {
            val intent = Intent(context, activity)
            context.startActivity(intent)
        }, modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null,
            modifier = Modifier.padding(end = 4.dp))
        Text("New Stopwatch Timer")
    }
}

@Composable
fun ListOfTimes(bundle: Bundle) {
    //Double exclamation marks is telling the compiler "This value is for sure, not null"
    //If it ends up being null, it'll throw a NullPointerException
    val savedTime = bundle.getStringArray("savedTime")!!

    //Benefit of lazy column is that it only loads text currently on the screen, rather than all at once
    //better for large data sets and has better scrolling performance
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .border(
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSecondaryContainer)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .width(400.dp)
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        //TextAlign is different from regular Alignment, know which one to use where
                        Text(text = formatTime(timeMilli = savedTime[1].toLong()), fontSize = 30.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text(savedTime[0], fontSize = 20.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

/* Why use Kotlin over Java?
 * Kotlin has more concise and readable code
 * Kotlin can be fully incorporated with Java features. Most libraries that work with Java will work with Kotlin too
 * Null safety: helps prevent null pointer exceptions. Allows the use of nullable and non-nullable types
 * Faster and easier implementation of common boilerplate code, like constructors
 * Access to android specific libraries to speed up development, like jetpack compose
 *
 * Downside is that it's newer, so there's less IDE support outside of Android Studio and fewer specific libraries
 */