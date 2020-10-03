package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

fun getEnergyOfPixel(x: Int, y: Int, image: BufferedImage): Double {
    val leftColor = Color(image.getRGB(x - 1, y))
    val rightColor = Color(image.getRGB(x + 1, y))
    val upperColor = Color(image.getRGB(x, y - 1))
    val bottomColor = Color(image.getRGB(x, y + 1))
    val xGradient = (leftColor.red.toDouble() - rightColor.red).pow(2.0) + (leftColor.green.toDouble() - rightColor.green.toDouble()).pow(2.0) + (leftColor.blue.toDouble() - rightColor.blue.toDouble()).pow(2.0)
    val yGradient = (upperColor.red.toDouble() - bottomColor.red.toDouble()).pow(2.0)
        (upperColor.green.toDouble() - bottomColor.green.toDouble()).pow(2.0)
        (upperColor.blue.toDouble() - bottomColor.blue.toDouble()).pow(2.0)
    return sqrt(xGradient + yGradient)
}