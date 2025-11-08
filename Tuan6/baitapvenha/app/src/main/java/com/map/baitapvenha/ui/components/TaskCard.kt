package com.map.baitapvenha.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.map.baitapvenha.R
import com.map.baitapvenha.data.Task


@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    // choose a gentle background color based on category or status
    val bgColor = when (task.category?.lowercase()) {
        "work" -> Color(0xFFFFC8C8) // light pink
        "health" -> Color(0xFFD8E8C5) // light green
        "meeting" -> Color(0xFFADD8E6) // light blue
        else -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = task.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                // lightweight checkbox placeholder to the right
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(4.dp))
                        .padding(2.dp)
                ) {
                    if (task.status == "In Progress") {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF4CAF50), shape = RoundedCornerShape(2.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                // Category chip
                AssistChip(
                    onClick = {},
                    label = { Text(task.category ?: "", style = MaterialTheme.typography.bodySmall) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = Color.White.copy(alpha = 0.6f))
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.status ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF424242)
                    )
                    if (task.priority == "High") {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_priority),
                            contentDescription = "High Priority",
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF212121),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
