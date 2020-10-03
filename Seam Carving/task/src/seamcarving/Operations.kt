package seamcarving

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
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

private val energyMap = mutableMapOf<String, Double>()

fun BufferedImage.greyScale(): BufferedImage {

    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            energyMap["$x - $y"] = getEnergyOfPixel(when (x) {
                0 -> 1
                this.width - 1 -> this.width - 2
                else -> x
            }, when (y) {
                0 -> 1
                this.height - 1 -> this.height - 2
                else -> y
            }, this)
        }
    }

    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            this.setNewColor(x, y)
        }
    }

    return this
}

var maxEnergyValue: Double = energyMap.values.maxOrNull() ?: 150.0

private fun BufferedImage.setNewColor(x: Int, y: Int) {
    val intensity = (255.0 * energyMap["$x - $y"]!! / maxEnergyValue).toInt()
    val newColor = Color(intensity, intensity, intensity)
    this.setRGB(x, y, newColor.rgb)
}