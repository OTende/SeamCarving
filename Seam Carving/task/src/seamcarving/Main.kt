package seamcarving

import seamcarving.methods.createFile
import seamcarving.methods.dropExcess
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val inName = args[args.indexOf("-in") + 1]
    val outName = args[args.indexOf("-out") + 1]
    val image = ImageIO.read(File(inName))
    val width = args[args.indexOf("-width") + 1].toInt()
    val height = args[args.indexOf("-height") + 1].toInt()
    createFile(image.dropExcess(width, height), outName)
}