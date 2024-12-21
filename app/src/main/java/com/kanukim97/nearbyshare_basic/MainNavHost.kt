package com.kanukim97.nearbyshare_basic

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Composable
fun MainNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = SearchDeviceRoute
    ) {
        composable<SearchDeviceRoute> {

        }
    }
}





@Serializable
data object SearchDeviceRoute
