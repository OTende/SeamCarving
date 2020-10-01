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
            val pixel = this.getRGB(x, y)
            var newColour = Color(pixel)
            newColour = Color(255 - newColour.red, 255 - newColour.green, 255 - newColour.blue)
            this.setRGB(x, y, newColour.rgb)
        }
    }
    return this
}

var energyMap = mutableMapOf<String, Double>()

fun BufferedImage.greyScale(): BufferedImage {
    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            energyMap["$x $y"] = getColorScheme(when (x) {
                0 -> 1
                else -> x
            }, when (y) {
                0 -> 1
                else -> y
            }, this)
        }
    }
    // TODO: Update every pixel by its intensity
}

private fun getMax(map: MutableCollection<Double>): Double {
    return map.maxOrNull()!!
}
