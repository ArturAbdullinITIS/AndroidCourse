package ru.itis.notes.presentation.ui.theme

import android.os.Build
import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)
private val RedColorScheme = lightColorScheme(
    primary = Red30,
    background = Red20,
    surface = Red10
)

private val BlueColorScheme = lightColorScheme(
    primary = Blue30,
    background = Blue20,
    surface = Blue10
)

private val GreenColorScheme = lightColorScheme(
    primary = Green30,
    background = Green20,
    surface = Green10
)

@Stable
object ThemeManager {
    var currentTheme: AppTheme by mutableStateOf(AppTheme.SYSTEM)
        private set

    fun setTheme(theme: AppTheme) {
        currentTheme = theme
    }
}


enum class AppTheme {
    RED, BLUE, GREEN, SYSTEM
}
@Composable
fun NotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when (ThemeManager.currentTheme) {
        AppTheme.RED -> RedColorScheme
        AppTheme.BLUE -> BlueColorScheme
        AppTheme.GREEN -> GreenColorScheme
        AppTheme.SYSTEM -> {
            when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                        context
                    )
                }

                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}