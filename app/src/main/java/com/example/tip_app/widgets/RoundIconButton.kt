package com.example.tip_app.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val IconbuttonSizeModifier = Modifier.size(40.dp)
@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.Blue.copy(alpha = 0.7f),
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 4.dp

){
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick.invoke() }
            .then(IconbuttonSizeModifier),
        shape = CircleShape,
        backgroundColor = backgroundColor,
        elevation = elevation) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Plus or Minus icon",
            tint = tint)
    }
}