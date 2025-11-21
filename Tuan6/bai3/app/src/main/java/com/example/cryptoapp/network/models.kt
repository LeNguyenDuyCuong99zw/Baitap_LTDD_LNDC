package com.example.cryptoapp.network

data class CryptoMarket(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Double,
    val price_change_percentage_24h: Double,
    val market_cap: Double,
    val image: String,
    val sparkline_in_7d: Sparkline? = null
)

data class Sparkline(
    val price: List<Double>
)

data class MarketChartResponse(
    val prices: List<List<Double>> // [timestamp, price]
)
