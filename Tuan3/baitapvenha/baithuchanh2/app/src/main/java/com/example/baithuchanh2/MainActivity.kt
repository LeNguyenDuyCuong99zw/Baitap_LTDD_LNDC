package com.example.baithuchanh2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.baithuchanh2.ui.theme.Baithuchanh2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Baithuchanh2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        var screenState by remember { mutableStateOf<Screen>(Screen.Onboarding) }

                        when (screenState) {
                            is Screen.Onboarding -> OnboardingScreen(onContinue = { screenState = Screen.ComponentsList })
                            is Screen.ComponentsList -> ComponentsListScreen(onItemClick = { screenState = it })
                            is Screen.TextDetail -> TextDetailScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.Images -> ImagesScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.TextField -> TextFieldScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.RowLayout -> RowLayoutScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.ColumnLayout -> ColumnLayoutScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.ButtonScreen -> ButtonScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.CheckboxScreen -> CheckboxScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.SwitchScreen -> SwitchScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.SliderScreen -> SliderScreen(onBack = { screenState = Screen.ComponentsList })
                            is Screen.LazyListScreen -> LazyListScreen(onBack = { screenState = Screen.ComponentsList })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun OnboardingScreen(modifier: Modifier = Modifier, onContinue: () -> Unit = {}) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top spacer with name and MSSV in the top-right
        Box(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 12.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Lê Nguyễn Duy Cường",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "080205008616",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Center content: image, title, description
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(3f)
        ) {
            // Load a drawable named ic_compose_logo (provided in res/drawable)
            val image: Painter = painterResource(id = com.example.baithuchanh2.R.drawable.ic_compose_logo)

            Image(
                painter = image,
                contentDescription = "Logo",
                modifier = Modifier.size(160.dp)
            )

            Text(
                text = "Jetpack Compose",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // Bottom button
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onContinue() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .size(width = 220.dp, height = 48.dp)
            ) {
                Text(text = "I'm ready", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Baithuchanh2Theme {
        Greeting("Android")
    }
}