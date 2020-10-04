package seamcarving

import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val inName = args[args.indexOf("-in") + 1]
    val outName = args[args.indexOf("-out") + 1]
    val image = ImageIO.read(File(inName))
    createFile(image.greyScale(), outName)
}