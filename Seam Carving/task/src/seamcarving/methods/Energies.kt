package seamcarving.methods

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

internal fun getEnergyOfPixel(image: BufferedImage): Array<Array<Double>> {
    val energy = Array(image.width) { Array(image.height) { 0.0 } }
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val x1 = when(x) {
                0 -> 1
                image.width - 1 -> image.width -2
                else -> x
            }

            val leftColor = Color(image.getRGB(x1 - 1, y))
            val rightColor = Color(image.getRGB(x1 + 1, y))

            val y1 = when(y) {
                0 -> 1
                image.height - 1 -> image.height -2
                else -> y
            }
            val upperColor = Color(image.getRGB(x, y1 - 1))
            val bottomColor = Color(image.getRGB(x, y1 + 1))
            val xGradient = (leftColor.red.toDouble() - rightColor.red).pow(2.0) + (leftColor.green.toDouble()
                    - rightColor.green.toDouble()).pow(2.0) + (leftColor.blue.toDouble() - rightColor.blue.toDouble()).pow(2.0)
            val yGradient = (upperColor.red.toDouble() - bottomColor.red.toDouble()).pow(2.0) + (upperColor.green.toDouble()
                    - bottomColor.green.toDouble()).pow(2.0) + (upperColor.blue.toDouble() - bottomColor.blue.toDouble()).pow(2.0)

            energy[x][y] = sqrt(xGradient + yGradient)
        }
    }
    return energy
}

internal fun getMaxEnergy(array: Array<Array<Double>>): Double {
    var max = array[0][0]
    for (i in array.indices) {
        for (j in array[i].indices) {
            if (array[i][j] > max) max = array[i][j]
        }
    }
    return max
}