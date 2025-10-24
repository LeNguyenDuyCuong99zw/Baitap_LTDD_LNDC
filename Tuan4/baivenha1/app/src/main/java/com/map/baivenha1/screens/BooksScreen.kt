package com.map.baivenha1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BooksScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Hệ thống\nQuản lý Thư viện",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Text(
            text = "Sinh viên",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Nguyen Thi B",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Danh sách sách",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = { /* Xử lý chọn sách */ }
                    )
                    Text(
                        text = "Sách 01",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Button(
            onClick = { /* Xử lý thêm sách */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Thêm")
        }

        NavigationBar {
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("home") },
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                label = { Text("Quản lý") }
            )
            NavigationBarItem(
                selected = true,
                onClick = { },
                icon = { Icon(imageVector = Icons.Default.List, contentDescription = null) },
                label = { Text("DS Sách") }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("students") },
                icon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                label = { Text("Sinh viên") }
            )
        }
    }
}