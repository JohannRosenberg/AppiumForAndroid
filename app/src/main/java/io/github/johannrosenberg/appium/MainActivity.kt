package io.github.johannrosenberg.appium

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.johannrosenberg.appium.ui.theme.AppiumForAndroidTheme
import io.github.johannrosenberg.appium.ui.utils.setTagAndId

class MainActivity : ComponentActivity() {
    private val ctx: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppiumForAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize().setTagAndId("scaffold")) { _ ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier.setTagAndId("myCoolButton"),
                            onClick = {
                            Toast.makeText(ctx, "Yeah, you just won a million dollars!", LENGTH_LONG).show()
                        },
                            content = {
                                Text("Test")
                            })
                    }
                }
            }
        }
    }
}

