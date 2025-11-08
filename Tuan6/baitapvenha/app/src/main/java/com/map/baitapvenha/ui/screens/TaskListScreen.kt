package com.map.baitapvenha.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.map.baitapvenha.R
import com.map.baitapvenha.ui.components.TaskCard
import com.map.baitapvenha.viewmodel.TaskListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, vm: TaskListViewModel = viewModel()) {
    val isLoading = vm.isLoading
    val tasks = vm.tasks
    var selectedTab by remember { mutableStateOf(0) }

    // listen for refresh flag from detail screen after delete using StateFlow
    val refreshFlow = navController.currentBackStackEntry?.savedStateHandle?.getStateFlow("refresh", false)
    val refresh by (refreshFlow?.collectAsState(initial = false) ?: remember { mutableStateOf(false) })

    LaunchedEffect(refresh) {
        if (refresh) {
            vm.fetchTasks()
            navController.currentBackStackEntry?.savedStateHandle?.set("refresh", false)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("SmartTasks", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { /* no-op for now */ }) {
                        Icon(painter = painterResource(R.drawable.ic_logo), contentDescription = "logo")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: navigate to create */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = "Calendar") },
                    label = { Text("Calendar") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_settings), contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (tasks.isEmpty()) {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(painter = painterResource(id = com.map.baitapvenha.R.drawable.ic_empty_tasks), contentDescription = null, tint = Color(0xFF9E9E9E), modifier = Modifier.size(64.dp))
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("No Tasks Yet!", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Stay productive — add something to do", style = MaterialTheme.typography.bodySmall, color = Color(0xFF666666))
                            }
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(tasks) { task ->
                            TaskCard(task = task) {
                                navController.navigate("detail/${task.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}
