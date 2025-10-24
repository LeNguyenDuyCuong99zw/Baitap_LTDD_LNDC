package com.map.baith2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.map.baith2.ui.theme.Baith2Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    // Kế thừa (Inheritance): MainActivity kế thừa từ ComponentActivity (Android)
    // -> MainActivity là một Activity cụ thể, thừa hưởng hành vi chung của Activity/ComponentActivity.
    //
    // Đa hình (Polymorphism) – ở đây thể hiện qua việc override onCreate:
    //  Khi Android framework gọi lifecycle method, phương thức onCreate() của lớp con
    //  (MainActivity) sẽ được thực thi thay vì phiên bản gốc của lớp cha.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Baith2Theme {
                OnboardingScreen()
            }
        }
    }
}

@Composable
fun OnboardingScreen() {
    // OnboardingScreen là một thành phần trừu tượng hoá giao diện (Abstraction):
    //  - Người dùng/khác phần mềm chỉ cần gọi `OnboardingScreen()` để hiển thị UI.
    //  - Chi tiết về cách vẽ, trạng thái và logic nội bộ được ẩn đi.
    //  => Đây là ví dụ về Trừu tượng (Abstraction) trong UI code.

    // Treat page == 0 as the splash screen (no navigation controls).
    val splashImage = R.drawable.anh1

    // Onboarding pages (these correspond to pages 1..n)
    // Đóng gói (Encapsulation): dữ liệu liên quan tới onboarding (images, titles, descriptions)
    // được gom bên trong hàm này — không thể truy cập từ bên ngoài hàm.
    val onboardImages = listOf(
        R.drawable.anh2,
        R.drawable.anh3,
        R.drawable.anh4
    )
    val titles = listOf(
        "Easy Time Management",
        "Increase Work Effectiveness",
        "Reminder Notification"
    )
    val descriptions = listOf(
        "With management based on priority and daily tasks, it will give you convenience in managing and determining the tasks that must be done first.",
        "Time management and the determination of more important tasks will give your job statistics better and always improve.",
        "This application also provides reminders so you don't forget assignments and can keep to the time you have set."
    )

    // page: 0 = splash, 1..onboardImages.size = onboarding pages
    // `page` là trạng thái encapsulated (đóng gói) nằm trong Composable này;
    // chỉ `OnboardingScreen` có quyền đọc/ghi trực tiếp. Đây là một dạng đóng gói trạng thái.
    var page by remember { mutableIntStateOf(0) }

    // Tự động chuyển từ splash (page == 0) sang onboarding đầu tiên (page = 1) sau 3 giây
    LaunchedEffect(page) {
        if (page == 0) {
            delay(5000L)
            page = 1
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top spacer / optional skip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // You can add a Skip button here if desired
            }

            // Content differs for splash (page == 0) and onboarding pages (page > 0)
            if (page == 0) {
                // Splash screen centered
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = splashImage),
                        contentDescription = "Splash",
                        modifier = Modifier
                            .size(220.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "UTH SmartTasks",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                // No dots or nav buttons on the splash page — user can swipe or wait to proceed.
            } else {
                val pageIndex = page - 1

                Image(
                    painter = painterResource(id = onboardImages[pageIndex]),
                    contentDescription = titles[pageIndex],
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Fit
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = titles[pageIndex],
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = descriptions[pageIndex],
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp)
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Dots
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 0 until onboardImages.size) {
                            val color = if (i == pageIndex) MaterialTheme.colorScheme.primary else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .size(if (i == pageIndex) 12.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .padding(4.dp)
                            )
                            if (i != onboardImages.lastIndex) {
                                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
                            }
                        }
                    }

                    // Buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Các Button/Composable ở đây là thành phần UI từ thư viện Material3.
                        //  Về đa hình (polymorphism):
                        //  - ComponentActivity / framework có thể gọi các method override (ví dụ onCreate).
                        //  - Ở mức UI, các Composable như Button/IconButton tuân theo API chung
                        //    (hàm nhận Modifier, onClick...) — tuy không phải là đa hình lớp truyền thống,
                        //    nhưng ý tưởng trừu tượng hoá/thay thế hành vi thông qua tham số/hàm là tương tự.
                        IconButton(onClick = { if (page > 0) page-- }) {
                            Text("‹")
                        }

                        if (page < onboardImages.size) {
                            Button(onClick = { page++ }) {
                                Text("Next")
                            }
                        } else {
                            Button(onClick = {

                            }) {
                                Text("Get Started")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    Baith2Theme {
        OnboardingScreen()
    }
}