package com.map.baitapvenha.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.Checkbox
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.map.baitapvenha.R
import com.map.baitapvenha.viewmodel.TaskDetailViewModel


@Composable
fun TaskDetailScreen(navController: NavController, taskId: Int, vm: TaskDetailViewModel = viewModel()) {
    val isLoading = vm.isLoading
    val task = vm.task

    LaunchedEffect(taskId) {
        vm.load(taskId)
    }

    if (isLoading && task == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp)) {

        // Top bar with back and delete
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }

            var showDeleteConfirm by remember { mutableStateOf(false) }

            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }

            if (showDeleteConfirm) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDeleteConfirm = false
                            task?.let {
                                vm.delete(it.id) { ok ->
                                    if (ok) {
                                        // notify list to refresh and go back
                                        navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }) { Text("Xóa") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirm = false }) { Text("Hủy") }
                    },
                    title = { Text("Xác nhận") },
                    text = { Text("Bạn có chắc muốn xóa công việc này?") }
                )
            }
        }

        task?.let {
            // Header với màu nền hồng nhạt và thông tin task
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFFC8C8),
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(it.title ?: "", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Category", style = MaterialTheme.typography.labelSmall)
                            Text(it.category ?: "Work", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Status", style = MaterialTheme.typography.labelSmall)
                            Text(it.status ?: "", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Priority", style = MaterialTheme.typography.labelSmall)
                            Text("High", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Subtasks section
            if (it.subtasks.isNotEmpty()) {
                Text("Subtasks", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                it.subtasks.forEach { sub ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = sub.isCompleted, onCheckedChange = { /* local only */ })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            sub.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF333333),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Attachments section
            Text("Attachments", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            
            // Attachments
            if (it.attachments.isNotEmpty()) {
                it.attachments.forEach { att ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_attachment),
                            contentDescription = null,
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = att.fileName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

        } ?: run {
            Text("Task not found")
        }
    }
}
