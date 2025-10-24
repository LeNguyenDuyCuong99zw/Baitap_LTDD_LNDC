@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.map.baith1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.map.baith1.ui.theme.Baith1Theme
import kotlin.random.Random
sealed class Screen(val route: String) {
    object Root : Screen("root")
    object List : Screen("list")
    object Detail : Screen("detail/{id}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Baith1Theme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Root.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Root.route) {
                RootScreen(onPush = { navController.navigate(Screen.List.route) })
            }

            composable(Screen.List.route) {
                ListScreen(
                    onItemClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DetailScreen(
                    id = id,
                    onBackToList = { navController.popBackStack() },
                    onBackToRoot = {
                        navController.navigate(Screen.Root.route) {
                            popUpTo(Screen.Root.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RootScreen(onPush: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // logo from drawable
        Image(
            painter = painterResource(id = com.map.baith1.R.drawable.ic_compose_logo),
            contentDescription = "logo",
            modifier = Modifier.size(160.dp)
        )

        Text(
            "Navigation",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )

        Text(
            "is a framework that simplifies the implementation of navigation between different UI components.",
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // big rounded gradient button similar to screenshot
        val gradient = Brush.horizontalGradient(listOf(Color(0xFF9BB6FF), Color(0xFF5166FF)))
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Surface(
                modifier = Modifier
                    .background(gradient, shape = RoundedCornerShape(24.dp))
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clickable { onPush() },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("PUSH", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(onItemClick: (Int) -> Unit, onBack: () -> Unit) {
    // Don't materialize 1_000_000 strings; generate the display text per-index.
    val itemCount = 1_000_000

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("LazyColumn") },
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Text("<", fontWeight = FontWeight.Bold)
                }
            }
        )

        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
            items(count = itemCount) { index ->
                val displayIndex = String.format("%02d", (index + 1) % 100)
                val quote = "${displayIndex} | The only way to do great work is to love what you do."
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFDFF6FF))
                            .padding(12.dp)
                            .clickable { onItemClick(index) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = quote, fontWeight = FontWeight.Medium)
                        }

                        // black circular chevron
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Black, shape = CircleShape)
                                .clickable { onItemClick(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = ">", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(id: Int, onBackToList: () -> Unit, onBackToRoot: () -> Unit) {
    val value = remember(id) { Random(id).nextInt(1_000_000_000) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        TopAppBar(
            title = { Text("Detail") },
            navigationIcon = {
                IconButton(onClick = onBackToList) {
                    Text("<", fontWeight = FontWeight.Bold)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("\"The only way to do great work is to love what you do\"", fontWeight = FontWeight.SemiBold)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(260.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                val blueGradient = Brush.verticalGradient(listOf(Color(0xFFBEE6FF), Color(0xFF5AB0FF)))
                Box(
                    modifier = Modifier
                        .background(blueGradient)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "\"The only way to do great work is to love what you do.\"",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // back to root big button
            val gradient = Brush.horizontalGradient(listOf(Color(0xFF9BB6FF), Color(0xFF5166FF)))
            Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
                Surface(modifier = Modifier
                    .background(gradient, shape = RoundedCornerShape(24.dp))
                    .padding(vertical = 14.dp), color = Color.Transparent) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBackToRoot() },
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("BACK TO ROOT", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Baith1Theme {
        RootScreen(onPush = {})
    }
}
