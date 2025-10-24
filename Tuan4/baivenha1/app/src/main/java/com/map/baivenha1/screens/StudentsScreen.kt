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
fun StudentsScreen(navController: NavController) {
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
            text = "Nguyen Van C",
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
            Text(
                text = "Bạn chưa mượn quyển sách nào\nNhấn 'Thêm' để bắt đầu hành trình đọc sách!",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
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
                selected = false,
                onClick = { navController.navigate("books") },
                icon = { Icon(imageVector = Icons.Default.List, contentDescription = null) },
                label = { Text("DS Sách") }
            )
            NavigationBarItem(
                selected = true,
                onClick = { },
                icon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                label = { Text("Sinh viên") }
            )
        }
    }
}