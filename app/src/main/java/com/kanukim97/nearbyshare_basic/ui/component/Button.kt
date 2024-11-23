package com.kanukim97.nearbyshare_basic.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kanukim97.nearbyshare_basic.ui.annotation.ComponentPreview
import com.kanukim97.nearbyshare_basic.ui.theme.Shapes

@Composable
fun BasicButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    shape: Shape = Shapes.medium
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text)
    }
}


@ComponentPreview
@Composable
fun PreviewBasicButton() {
    BasicButton(text = "onClick")
}