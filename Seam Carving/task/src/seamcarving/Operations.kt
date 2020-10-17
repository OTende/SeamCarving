package seamcarving

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun drawImage(width: Int, height: Int): BufferedImage {
    val image = BufferedImage(width, height, TYPE_INT_RGB)
    image.createGraphics().background = Color.BLACK
    image.createGraphics().color = Color.RED
    val g2d: Graphics2D = image.createGraphics()
    g2d.color = Color.RED
    g2d.drawLine(0, 0, width - 1, height - 1)
    g2d.drawLine(0, height - 1, width - 1, 0)
    return image
}

fun createFile(image: BufferedImage, name: String) {
    ImageIO.write(image, "png", File(name))
}

fun BufferedImage.negate(): BufferedImage {
    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            var newColor = Color(this.getRGB(x, y))
            newColor = Color(255 - newColor.red, 255 - newColor.green, 255 - newColor.blue)
            this.setRGB(x, y, newColor.rgb)
        }
    }
    return this
}

fun BufferedImage.greyScale(): BufferedImage {
    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            this.setNewColor(x, y)
        }
    }
    return this
}

private fun BufferedImage.setNewColor(x: Int, y: Int) {
    val energy = getEnergyOfPixel(this)
    val maxEnergyValue = getMaxEnergy(energy)
    val intensity = (255.0 * energy[x][y]!! / maxEnergyValue).toInt()
    this.setRGB(x, y, Color(intensity, intensity, intensity).rgb)
}

fun BufferedImage.findTheSeam(): BufferedImage {
    val energy = getEnergyOfPixel(this)
    var lowestBottom = Double.MAX_VALUE
    var oldSeamTotal = Double.MAX_VALUE
    val stack = Stack<Int>()
    val oldStack = Stack<Int>()
    val jLast = energy[0].lastIndex
    val iLast = energy.lastIndex
    val setRed = { i: Int, j: Int -> this.setRGB(i, j, Color(255, 0, 0).rgb) }

    for (j in 1..jLast) {
        for (i in 0..iLast) {
            var add = energy[i][j - 1]
            if (i != 0 && energy[i - 1][j - 1] < add) add = energy[i - 1][j - 1]
            if (i != iLast && energy[i + 1][j - 1] < add) add = energy[i + 1][j - 1]
            energy[i][j] += add
            if (j == jLast && energy[i][j] < lowestBottom) lowestBottom = energy[i][j]
        }
    }

    for (i in 0..energy.lastIndex) {
        if (energy[i][jLast] == lowestBottom) {
            stack.push(i)
            var seamTotal = energy[i][jLast]
            var index = i
            for (j in jLast - 1 downTo 0) {
                var lowIndex = index
                if (index != 0 && energy[index - 1][j] < energy[index][j]) lowIndex -= 1
                if (index != iLast && energy[index + 1][j] < energy[lowIndex][j]) lowIndex = index + 1
                index = lowIndex
                stack.push(index)
                seamTotal += energy[index][j]
            }
            if (seamTotal < oldSeamTotal) {
                if (oldStack.isNotEmpty()) oldStack.clear()
                oldSeamTotal = seamTotal
                oldStack.addAll(stack)
            }
            stack.clear()
        }
    }
    for (j in 0..jLast) setRed(oldStack.pop(), j)

    return this
}
