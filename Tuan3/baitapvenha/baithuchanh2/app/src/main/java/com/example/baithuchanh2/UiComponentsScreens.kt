package com.example.baithuchanh2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

sealed class Screen {
    object Onboarding : Screen()
    object ComponentsList : Screen()
    object TextDetail : Screen()
    object Images : Screen()
    object TextField : Screen()
    object RowLayout : Screen()
    object ColumnLayout : Screen()
    object ButtonScreen : Screen()
    object CheckboxScreen : Screen()
    object SwitchScreen : Screen()
    object SliderScreen : Screen()
    object LazyListScreen : Screen()
}

data class ComponentItem(
    val title: String,
    val description: String,
    val screen: Screen,
    val section: String
)

@Composable
fun ComponentsListScreen(onItemClick: (Screen) -> Unit) {
    val items = listOf(
        ComponentItem("Text", "Displays text", Screen.TextDetail, "Display"),
        ComponentItem("Image", "Displays an image", Screen.Images, "Display"),
        ComponentItem("TextField", "Input field for text", Screen.TextField, "Input"),
        ComponentItem("PasswordField", "Input field for passwords", Screen.TextField, "Input"),
        ComponentItem("Column", "Arranges elements vertically", Screen.ColumnLayout, "Layout"),
        ComponentItem("Row", "Arranges elements horizontally", Screen.RowLayout, "Layout"),
        ComponentItem("Button", "Nút bấm (Button)", Screen.ButtonScreen, "Controls"),
        ComponentItem("Checkbox", "Hộp chọn (Checkbox)", Screen.CheckboxScreen, "Controls"),
        ComponentItem("Switch", "Công tắc (Switch)", Screen.SwitchScreen, "Controls"),
        ComponentItem("Slider", "Thanh trượt (Slider)", Screen.SliderScreen, "Controls"),
        ComponentItem("LazyList", "Danh sách cuộn (LazyColumn)", Screen.LazyListScreen, "Layout")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "UI Components List",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Group items by section
        val groupedItems = items.groupBy { it.section }
        groupedItems.forEach { (section, sectionItems) ->
            item {
                Text(
                    text = section,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(sectionItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item.screen) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE3F2FD))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // Add the "Tự tìm hiểu" section at bottom
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFEBEE))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Tự tìm hiểu\nTìm ra tất cả các thành phần UI cơ bản",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE57373)
                    )
                }
            }
        }
    }
}

@Composable
fun TextDetailScreen(onBack: () -> Unit = {}) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "<",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1E88E5),
                    modifier = Modifier.clickable { onBack() }
                )
                Text(
                    text = "Components Detail",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                // Invisible text for balanced spacing
                Text(
                    text = "<",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Transparent
                )
            }
            
            // descriptor card removed to match design

            // Center the annotated text in the remaining space
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = buildAnnotatedString {
                        append("The ")
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append("quick ")
                        }
                        withStyle(SpanStyle(color = Color(0xFF795548))) {
                            append("Brown ")
                        }
                        append("fox j u m p s ")
                        withStyle(SpanStyle(background = Color(0xFFFFEB3B))) {
                            append("over")
                        }
                        append("\nthe ")
                        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("lazy")
                        }
                        append(" dog.")
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }
        }
    }
}

@Composable
fun ImagesScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1E88E5),
                modifier = Modifier.clickable { onBack() }
            )
            Text(
                text = "Components Detail",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            // Invisible text for balanced spacing
            Text(
                text = "<",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Transparent
            )
        }
        
        // descriptor card removed to match design

        // URL của ảnh UTH
        Text(
            text = "https://giaothongvantaitphcm.edu.vn/templates/themes/images/logo.png?v=13.1.5",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_compose_logo), // Placeholder, cần thay bằng AsyncImage với Coil
                contentDescription = "UTH Campus",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = "In app",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
}
    }

@Composable
fun TextFieldScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1E88E5),
                modifier = Modifier.clickable { onBack() }
            )
            Text(
                text = "Components Detail",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            // Invisible text for balanced spacing
            Text(
                text = "<",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Transparent
            )
        }

        // descriptor card removed to match design

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Thông tin nhập...",
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Text(
            text = "Tự động cập nhật đã liệu theo textfield",
            color = Color(0xFFE53935),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 4.dp)
        )
}
    }

@Composable
fun RowLayoutScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1E88E5),
                modifier = Modifier.clickable { onBack() }
            )
            Text(
                text = "Components Detail",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            // Invisible text for balanced spacing
            Text(
                text = "<",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Transparent
            )
        }

        // descriptor card removed to match design

        // Regular grid of boxes
        repeat(4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .background(Color(0xFFBBDEFB), RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        // Highlighted row with border
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .background(Color(0xFFBBDEFB), RoundedCornerShape(8.dp))
                        .padding(2.dp) // For border effect
                        .background(Color(0xFF2196F3), RoundedCornerShape(8.dp))
                )
            }
        }
}
    }

@Composable
fun ColumnLayoutScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1E88E5),
                modifier = Modifier.clickable { onBack() }
            )
            Text(
                text = "Components Detail",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            // Invisible text for balanced spacing
            Text(
                text = "<",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Transparent
            )
        }

        // descriptor card removed to match design

        // Regular boxes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFFC8E6C9), RoundedCornerShape(8.dp))
        )
        
        // Highlighted box (darker green)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFF66BB6A), RoundedCornerShape(8.dp))
        )
        
        // Regular box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFFC8E6C9), RoundedCornerShape(8.dp))
        )
}
    }

@Composable
fun ButtonScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "<", color = Color(0xFF1E88E5), modifier = Modifier.clickable { onBack() })
            Text(text = "Button", style = MaterialTheme.typography.titleLarge)
            Text(text = "<", color = Color.Transparent)
        }

        var count by remember { mutableStateOf(0) }
        androidx.compose.material3.Button(onClick = { count++ }) {
            Text("Clicked: $count")
        }
    }
}

@Composable
fun CheckboxScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "<", color = Color(0xFF1E88E5), modifier = Modifier.clickable { onBack() })
            Text(text = "Checkbox", style = MaterialTheme.typography.titleLarge)
            Text(text = "<", color = Color.Transparent)
        }

        var checked by remember { mutableStateOf(false) }
        androidx.compose.material3.Checkbox(checked = checked, onCheckedChange = { checked = it })
        Text(if (checked) "Đã chọn" else "Chưa chọn")
    }
}

@Composable
fun SwitchScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "<", color = Color(0xFF1E88E5), modifier = Modifier.clickable { onBack() })
            Text(text = "Switch", style = MaterialTheme.typography.titleLarge)
            Text(text = "<", color = Color.Transparent)
        }

        var on by remember { mutableStateOf(true) }
        androidx.compose.material3.Switch(checked = on, onCheckedChange = { on = it })
        Text(if (on) "Bật" else "Tắt")
    }
}

@Composable
fun SliderScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "<", color = Color(0xFF1E88E5), modifier = Modifier.clickable { onBack() })
            Text(text = "Slider", style = MaterialTheme.typography.titleLarge)
            Text(text = "<", color = Color.Transparent)
        }

        var value by remember { mutableStateOf(0.5f) }
        androidx.compose.material3.Slider(value = value, onValueChange = { value = it })
        Text("Giá trị: ${String.format("%.2f", value)}")
    }
}

@Composable
fun LazyListScreen(onBack: () -> Unit = {}) {
    val items = (1..30).map { "Item #$it" }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "<", color = Color(0xFF1E88E5), modifier = Modifier.clickable { onBack() })
            Text(text = "LazyList", style = MaterialTheme.typography.titleLarge)
            Text(text = "<", color = Color.Transparent)
        }
        LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items) { item ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Text(item, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}
