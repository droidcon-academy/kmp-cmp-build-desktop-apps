package com.droidcon.notedock.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun convertTimestampToDateString(timestampMillis: Long, pattern: String): String {
    val date = Date(timestampMillis)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault()) // Or specify a desired locale
    return formatter.format(date)
}



class Utils {
    companion object {
        /**
         * Generates random sentences
         */
        fun generateRandomSentence(): String{
            val subjects = listOf(
                "We",
                "Police",
                "People",
                "Developers",
                "Countries"
            )

            val objects = listOf(
                "buildings",
                "books",
                "computers",
                "AI",
                "technologies",
                "Jetpack Compose"
            )

            val verbs = listOf(
                "love",
                "hate",
                "destroy",
                "program",
                "fix",
                "make"
            )

            val adverbs = listOf(
                "foolishly",
                "intelligently",
                "quickly",
                "lazily",
                "slowly"
            )

            val adjectives = listOf(
                "beautiful",
                "big",
                "small",
                "lovely",
                "ridiculous"
            )

            return subjects.random() + " " + adverbs.random() + " " + verbs.random() + " " + adjectives.random() + " " + objects.random()
        }
    }

}


/**
 * Creates a dashed border. [See this](https://medium.com/@kappdev/dashed-borders-in-jetpack-compose-a-comprehensive-guide-de990a944c4c)
 */
fun Modifier.dashedBorder(
    brush: Brush,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
    ) = this.drawWithContent {
        val outline = shape.createOutline(size, layoutDirection, density = this)
        val dashedStroke = Stroke(
            cap = cap,
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx())
            )
        )
        drawContent()

        drawOutline(
            outline = outline,
            style = dashedStroke,
            brush = brush
        )
    }

