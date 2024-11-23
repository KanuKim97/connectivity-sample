package com.kanukim97.nearbyshare_basic.utils

import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionType
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.Strategy

/**
 * 2024.11.09
 *
 * ConnectionOptions
 *
 * Gms near by connection ConnectionOptions
 *
 * @property advertisingOptions Declare AdvertisingOptions
 * @property discoveryOptions Declare DiscoveryOptions
 *
 * @author KanuKim97
 */
object ConnectionOptions {
    val advertisingOptions = AdvertisingOptions.Builder()
        .setStrategy(Strategy.P2P_POINT_TO_POINT)
        .setLowPower(true)
        .setConnectionType(ConnectionType.BALANCED)
        .build()

    val discoveryOptions = DiscoveryOptions.Builder()
        .setStrategy(Strategy.P2P_POINT_TO_POINT)
        .setLowPower(true)
        .build()
}