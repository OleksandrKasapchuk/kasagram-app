package com.kasagram.ui.theme


import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = TextMain,           // Акцентний колір (кнопки, лайки)
    background = BgDark,           // Головний фон екрана
    surface = CardDark,            // Фон карток (постів)
    outline = BorderColor,         // Рамки та лінії
    onBackground = TextMain,       // Колір тексту на головному фоні
    onSurface = TextMain,          // Колір тексту на картках
    onSurfaceVariant = TextSecondary, // Другорядний текст
    secondary = TextSecondary,
    tertiary = AccentRed,
    primaryContainer = MessageBlue,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun KasagramTheme(
    darkTheme: Boolean = true, // Можеш примусово поставити true, якщо хочеш тільки темну тему
    dynamicColor: Boolean = false, // ВСТАВ ТУТ FALSE, щоб твої кольори працювали
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Якщо dynamicColor = false, цей блок пропуститься
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}