package com.kanukim97.nearbyshare_basic.constant

import android.Manifest


object Constant {
    const val DEBUG_TAG = "LOG_DEBUG"
    const val ERROR_TAG = "LOG_ERROR"
    const val WARN_TAG = "LOG_WARN"
    const val INFO_TAG = "LOG_INFO"

    const val SERVICE_ID = "com.kanukim97.nearbyshare_basic.SERVICE_ID"

    const val BLUETOOTH_PERMISSION = Manifest.permission.BLUETOOTH
    const val BLUETOOTH_SCAN_PERMISSION = Manifest.permission.BLUETOOTH_SCAN
    const val BLUETOOTH_ADVERTISE_PERMISSION = Manifest.permission.BLUETOOTH_ADVERTISE
    const val NEARBY_WIFI_DEVICES_PERMISSION = Manifest.permission.NEARBY_WIFI_DEVICES

    const val ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    const val ACCESS_COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
}