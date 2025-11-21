package com.example.cryptoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import com.example.cryptoapp.network.CryptoMarket
import com.example.cryptoapp.viewmodel.CryptoViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoChartScreen(
    viewModel: CryptoViewModel,
    cryptoId: String,
    onBack: () -> Unit
) {
    val chartData by viewModel.chartData.collectAsState()
    val cryptoList by viewModel.cryptoList.collectAsState()

    val crypto: CryptoMarket? = cryptoList.find { it.id == cryptoId }

    LaunchedEffect(cryptoId) {
        viewModel.fetchChart(cryptoId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // === Top App Bar với nút Back ===
        TopAppBar(
            title = { Text(crypto?.symbol?.uppercase() ?: "Crypto", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1E1E1E)
            )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // === Thông tin coin ===
            crypto?.let { c ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = c.symbol.uppercase(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$${c.current_price}",
                            color = if (c.price_change_percentage_24h >= 0) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "24h Change: ${c.price_change_percentage_24h}%",
                            color = if (c.price_change_percentage_24h >= 0) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Market Cap: $${c.market_cap}",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // === Biểu đồ ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                if (chartData.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Cyan)
                    }
                } else {
                    AndroidView(factory = { context ->
                        LineChart(context).apply {
                            description.isEnabled = false
                            axisRight.isEnabled = false
                            axisLeft.textColor = android.graphics.Color.WHITE
                            xAxis.textColor = android.graphics.Color.WHITE
                            legend.isEnabled = false
                            setTouchEnabled(true)
                            setPinchZoom(true)
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)

                            // Chia timestamp dùng index
                            val entries = chartData.mapIndexed { index, pair ->
                                Entry(index.toFloat(), pair.second.toFloat())
                            }

                            val dataSet = LineDataSet(entries, crypto?.symbol?.uppercase() ?: cryptoId).apply {
                                color = android.graphics.Color.CYAN
                                lineWidth = 2f
                                setDrawCircles(false)
                                setDrawValues(false)
                            }

                            data = LineData(dataSet)
                            invalidate()
                        }
                    })
                }
            }
        }
    }
}
