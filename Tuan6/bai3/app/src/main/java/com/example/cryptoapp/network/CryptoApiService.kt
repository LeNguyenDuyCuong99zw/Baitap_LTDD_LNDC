package com.example.cryptoapp.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoApiService {

    @GET("coins/markets")
    suspend fun getCryptoMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("ids") ids: String? = null,
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = true
    ): List<CryptoMarket>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("days") days: Int,
        @Query("interval") interval: String
    ): MarketChartResponse
}
