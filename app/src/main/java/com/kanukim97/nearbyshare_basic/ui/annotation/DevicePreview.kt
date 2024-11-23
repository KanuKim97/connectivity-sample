package com.kanukim97.nearbyshare_basic.ui.annotation

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * 2024.11.09
 *
 * ## DevicePreview
 *
 * If you want to see screen composable preview
 *
 * Using this annotation it will be show 3 different type device preview your composable screen
 *
 * ### Usage
 * ```
 * @DevicePreview
 * @Composable
 * fun PreviewScreen() {
 *     // ScreenComposable
 *     BasicScreen()
 * }
 * ```
 *
 * @author KanuKim97
 */
@Preview(
    name = "PIXEL",
    group = "Device",
    showBackground = true,
    locale = "en",
    device = Devices.PIXEL
)
@Preview(
    name = "TABLET",
    group = "Device",
    showBackground = true,
    locale = "en",
    device = Devices.TABLET
)
@Preview(
    name = "PIXEL_FOLD",
    group = "Device",
    showBackground = true,
    locale = "en",
    device = Devices.PIXEL_FOLD
)
annotation class DevicePreview