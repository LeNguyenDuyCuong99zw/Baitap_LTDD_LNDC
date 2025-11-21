package com.example.cryptoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptoapp.network.CryptoMarket
import com.example.cryptoapp.viewmodel.CryptoViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter

@Composable
fun CryptoListScreen(
    onItemClick: (CryptoMarket) -> Unit,
    viewModel: CryptoViewModel = viewModel()
) {
    val cryptoList by viewModel.cryptoList.collectAsState()
    val favorites = cryptoList.filter { it.id in listOf("bitcoin", "ethereum") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(12.dp)
    ) {
        // === Top Banner ===
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "🔥 Breaking Crypto News",
                    color = Color.Cyan,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === Favorites ===
        if (favorites.isNotEmpty()) {
            Text(
                "Favorites",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(favorites) { crypto ->
                    CoinCardMini(crypto, onItemClick)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // === All Fluctuations ===
        Text(
            "All Fluctuations",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(cryptoList) { crypto ->
                CryptoRow(crypto, onItemClick)
            }
        }
    }
}

@Composable
fun CoinCardMini(crypto: CryptoMarket, onClick: (CryptoMarket) -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(140.dp)
            .clickable { onClick(crypto) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    crypto.image?.let { url ->
                        Image(
                            painter = rememberAsyncImagePainter(url),
                            contentDescription = crypto.symbol,
                            modifier = Modifier.size(24.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        crypto.symbol.uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "$${crypto.current_price}",
                    color = if (crypto.price_change_percentage_24h >= 0) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${crypto.price_change_percentage_24h}%",
                    color = if (crypto.price_change_percentage_24h >= 0) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                )
            }
            MiniLineChart(crypto.sparkline_in_7d?.price ?: emptyList())
        }
    }
}

@Composable
fun CryptoRow(crypto: CryptoMarket, onClick: (CryptoMarket) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick(crypto) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo coin
            crypto.image?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = crypto.symbol,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(crypto.symbol.uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("$${crypto.current_price}", color = Color.White)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${crypto.price_change_percentage_24h}%",
                    color = if (crypto.price_change_percentage_24h >= 0) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                MiniLineChartRow(crypto.sparkline_in_7d?.price ?: emptyList())
            }
        }
    }
}

@Composable
fun MiniLineChart(prices: List<Double>) {
    if (prices.isEmpty()) return
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)) {
        val maxPrice = prices.maxOrNull() ?: 0.0
        val minPrice = prices.minOrNull() ?: 0.0
        val range = maxPrice - minPrice
        if (range == 0.0) return@Canvas

        val stepX = size.width / (prices.size - 1)
        val path = Path()
        prices.forEachIndexed { index, price ->
            val x = index * stepX
            val y = size.height - ((price - minPrice) / range * size.height).toFloat()
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = Color.Cyan, style = Stroke(width = 2.dp.toPx()))
    }
}

// Mini chart nhỏ trong row All Fluctuations
@Composable
fun MiniLineChartRow(prices: List<Double>) {
    if (prices.isEmpty()) return
    Canvas(modifier = Modifier
        .width(60.dp)
        .height(20.dp)) {
        val maxPrice = prices.maxOrNull() ?: 0.0
        val minPrice = prices.minOrNull() ?: 0.0
        val range = maxPrice - minPrice
        if (range == 0.0) return@Canvas

        val stepX = size.width / (prices.size - 1)
        val path = Path()
        prices.forEachIndexed { index, price ->
            val x = index * stepX
            val y = size.height - ((price - minPrice) / range * size.height).toFloat()
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = Color.Cyan, style = Stroke(width = 1.5.dp.toPx()))
    }
}
