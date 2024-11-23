package com.kanukim97.nearbyshare_basic.ui.annotation

import androidx.compose.ui.tooling.preview.Preview

/**
 * 2024.11.09
 *
 * ## ComponentPreview
 *
 * If you want to see component composable preview
 *
 * Using this annotation it will be show your composable component preview
 *
 * ### Usage
 * ```
 * @ComponentPreview
 * @Composable
 * fun PreviewButton() {
 *    // Composable
 *    Text("Greeting")
 * }
 * ```
 *
 * @author KanuKim97
 */
@Preview(
    group = "Component",
    showBackground = true,
    locale = "en"
)
annotation class ComponentPreview
