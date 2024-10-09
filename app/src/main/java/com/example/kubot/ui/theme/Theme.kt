package com.example.kubot.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.kubot.R

@Composable
private fun darkColorPalette(): Colors {

    return darkColors(
        primary = Color.White,
        onPrimary = Color.Black,
        primaryVariant = KubotPurple,
        secondary = colorResource(id = R.color.kubot_green),
        onSecondary = Color.White,
        background = Color.Black,
        surface = Color.Black,
        onSurface = Color.White,
        onBackground = Color.White,
    )
}

@Composable
private fun lightColorPalette(): Colors {
    return lightColors(
        primary = Color.Black,
        onPrimary = Color.White,
        primaryVariant = KubotPurple,
        secondary = colorResource(id = R.color.kubot_green),
        onSecondary = Color.White,
        background = Color.White,
        surface = Color.White,
        onSurface = Color.Black,
        onBackground = Color.Black,
    )
}

@Composable
fun KubotTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        darkColorPalette()
    } else {
        lightColorPalette()
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


@Composable
fun textEntryFieldTextStyle() = Typography.h5.copy(
    fontFamily = fonts,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = colors.primary,
)