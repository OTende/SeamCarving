package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

fun getColorScheme(x: Int, y: Int, image: BufferedImage): Double {
    val leftColor = Color(image.getRGB(x - 1, y))
    val rightColor = Color(image.getRGB(x + 1, y))
    val upperColor = Color(image.getRGB(x, y + 1))
    val bottomColor = Color(image.getRGB(x, y - 1))
    val xGradient = (leftColor.red - rightColor.red.toDouble()).pow(2.0)
        (leftColor.green - rightColor.green.toDouble()).pow(2.0)
        (leftColor.blue - rightColor.blue.toDouble()).pow(2.0)
    val yGradient = (upperColor.red - bottomColor.red.toDouble()).pow(2.0)
        (upperColor.green - rightColor.green.toDouble()).pow(2.0)
        (upperColor.blue - rightColor.blue.toDouble()).pow(2.0)
    return sqrt(xGradient + yGradient)
}

fun setIntensityColor(x: Int, y: Int) {
    // TODO: Just do anything here
}
