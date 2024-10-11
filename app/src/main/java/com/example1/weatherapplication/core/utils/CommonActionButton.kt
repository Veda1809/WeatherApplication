package com.example1.weatherapplication.core.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example1.weatherapplication.R

@Composable
fun CommonActionButton(
    modifier: Modifier = Modifier,
    buttonText: String = stringResource(id = R.string.submit),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = Color.Blue,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .noRippleClickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonText,
            style = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.W200,
                color = Color.White,
                fontSize = 16.sp
            )
        )

    }
}