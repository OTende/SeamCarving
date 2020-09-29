package seamcarving

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO

var width: Int = 0
var height: Int = 0
var name: String = ""

fun main() {
    createImage()
    val image = BufferedImage(width, height, TYPE_INT_RGB)
    draw(image)
    createFile(image)
}

fun createImage() {
    println("Enter rectangle width:")
    width = readLine()!!.toInt()
    println("Enter rectangle height:")
    height = readLine()!!.toInt()
    println("Enter output image name:")
    name = readLine()!!
}

fun draw(image: BufferedImage) {
    image.createGraphics().background = Color.BLACK
    image.createGraphics().color = Color.RED
    val g2d: Graphics2D = image.createGraphics()
    g2d.color = Color.RED
    g2d.drawLine(0, 0, width - 1, height - 1)
    g2d.drawLine(0, height - 1, width - 1, 0)
}

fun createFile(image: BufferedImage) {
    val outFile = File(name)
    ImageIO.write(image, "png", outFile)
}

fun aaa() {

}