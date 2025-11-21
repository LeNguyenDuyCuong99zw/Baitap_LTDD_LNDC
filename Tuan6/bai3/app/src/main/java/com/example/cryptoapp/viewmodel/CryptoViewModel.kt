package com.example.cryptoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.network.CryptoMarket
import com.example.cryptoapp.network.MarketChartResponse
import com.example.cryptoapp.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CryptoViewModel : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoMarket>>(emptyList())
    val cryptoList: StateFlow<List<CryptoMarket>> get() = _cryptoList

    private val _chartData = MutableStateFlow<List<Pair<Long, Double>>>(emptyList())
    val chartData: StateFlow<List<Pair<Long, Double>>> get() = _chartData

    init {
        startRealtimeUpdate()
    }

    /** Fetch chart chỉ khi mở coin chi tiết */
    fun fetchChart(id: String) {
        viewModelScope.launch {
            try {
                val response: MarketChartResponse =
                    RetrofitClient.api.getMarketChart(id, "usd", 1, "hourly")
                _chartData.value = response.prices.map { it[0].toLong() to it[1] }
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    println("HTTP 429: Too many requests. Please wait before retrying.")
                } else {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /** Cập nhật danh sách coin định kỳ (15s) */
    private fun startRealtimeUpdate() {
        viewModelScope.launch {
            while (true) {
                try {
                    val list = RetrofitClient.api.getCryptoMarkets(
                        "usd",
                        "bitcoin,ethereum,cardano,solana,polkadot"
                    )
                    _cryptoList.value = list
                } catch (e: HttpException) {
                    if (e.code() == 429) {
                        println("HTTP 429: Too many requests. Skipping this fetch.")
                    } else {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(15000) // 15 giây/lần để tránh bị rate limit
            }
        }
    }
}
