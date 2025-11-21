package com.example.cryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptoapp.screens.CryptoChartScreen
import com.example.cryptoapp.screens.CryptoListScreen
import com.example.cryptoapp.viewmodel.CryptoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val cryptoViewModel: CryptoViewModel = viewModel()

            NavHost(navController = navController, startDestination = "list") {

                // === List screen ===
                composable("list") {
                    CryptoListScreen(
                        onItemClick = { crypto ->
                            navController.navigate("chart/${crypto.id}")
                        },
                        viewModel = cryptoViewModel
                    )
                }

                // === Chart screen ===
                composable("chart/{cryptoId}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("cryptoId") ?: ""
                    CryptoChartScreen(
                        viewModel = cryptoViewModel,
                        cryptoId = id,
                        onBack = { navController.popBackStack() } // ← quay về list
                    )
                }
            }
        }
    }
}
