package seamcarving.methods

import java.awt.Color
import java.awt.Graphics2D
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

private fun BufferedImage.findTheVerticalSeam(): BufferedImage {
    return this.rotate().findTheHorizontalSeam().rotate()
}

private fun BufferedImage.rotate(): BufferedImage {
    val newImage = BufferedImage(this.height, this.width, TYPE_INT_RGB)
    for (i in 0 until this.width) {
        for (j in 0 until this.height) {
            newImage.setRGB(j, i, Color(this.getRGB(i, j)).rgb)
        }
    }
    return newImage
}

private fun BufferedImage.findTheHorizontalSeam(): BufferedImage {
    val energy = getEnergyOfPixel(this)
    var lowestBottom = Double.MAX_VALUE
    var oldSeamTotal = Double.MAX_VALUE
    val stack = Stack<Int>()
    val oldStack = Stack<Int>()
    val yLast = energy[0].lastIndex
    val xLast = energy.lastIndex

    for (y in 1..yLast) {
        for (x in 0..xLast) {
            var add = energy[x][y - 1]
            if (x != 0 && energy[x - 1][y - 1] < add) add = energy[x - 1][y - 1]
            if (x != xLast && energy[x + 1][y - 1] < add) add = energy[x + 1][y - 1]
            energy[x][y] += add
            if (y == yLast && energy[x][y] < lowestBottom) lowestBottom = energy[x][y]
        }
    }

    for (x in 0..energy.lastIndex) {
        if (energy[x][yLast] == lowestBottom) {
            stack.push(x)
            var seamTotal = energy[x][yLast]
            var index = x
            for (y in yLast - 1 downTo 0) {
                var lowIndex = index
                if (index != 0 && energy[index - 1][y] < energy[index][y]) lowIndex -= 1
                if (index != xLast && energy[index + 1][y] < energy[lowIndex][y]) lowIndex = index + 1
                index = lowIndex
                stack.push(index)
                seamTotal += energy[index][y]
            }
            if (seamTotal < oldSeamTotal) {
                if (oldStack.isNotEmpty()) oldStack.clear()
                oldSeamTotal = seamTotal
                oldStack.addAll(stack)
            }
            stack.clear()
        }
    }
    val newImage = BufferedImage(this.width - 1, this.height, TYPE_INT_RGB)
    for (j in 0..yLast) {
        val skipNum = oldStack.pop()
        var toSkip = false
        for (i in 0..xLast) {
            if (i == skipNum) {
                toSkip = true
                continue
            }
            newImage.setRGB(if (toSkip) i - 1 else i, j, this.getRGB(i, j))
        }
    }
    return newImage
}

fun BufferedImage.dropExcess(width: Int, height: Int): BufferedImage {
    var newImage = this
    for (i in 0 until width) {
        newImage = newImage.findTheHorizontalSeam()
    }
    for (i in 0 until height) {
        newImage = newImage.findTheVerticalSeam()
    }
    return newImage
}