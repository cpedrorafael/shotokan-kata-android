import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

@Composable
fun CustomLinearProgressIndicator(progress: Float, modifier: Modifier = Modifier) {
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f

    Canvas(modifier = modifier.height(4.dp)) {
        val strokeWidth = size.height // Use the Canvas height as the stroke width for the bar


        // Draw the track
        drawLine(
            color = if(isDarkTheme) Color(50 , 50, 50) else Color(200, 200, 200),
            start = Offset(x = 0f, y = center.y),
            end = Offset(x = size.width, y = center.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Draw the progress
        drawLine(
            color = Color(50, 205, 50),
            start = Offset(x = 0f, y = center.y),
            end = Offset(x = size.width * progress, y = center.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
