package com.map.baitapvenha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.map.baitapvenha.ui.screens.TaskDetailScreen
import com.map.baitapvenha.ui.screens.TaskListScreen
import com.map.baitapvenha.ui.theme.BaitapvenhaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaitapvenhaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        TaskListScreen(navController = navController)
                    }
                    composable("detail/{id}") { backStackEntry ->
                        val idStr = backStackEntry.arguments?.getString("id")
                        val id = idStr?.toIntOrNull() ?: 0
                        TaskDetailScreen(navController = navController, taskId = id)
                    }
                }
            }
        }
    }
}

@Composable
fun GreetingPreview() {
    BaitapvenhaTheme {
        // preview uses list screen
        Greeting()
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
}