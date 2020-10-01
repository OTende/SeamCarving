package seamcarving

import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val inName: String = args[args.indexOf("-in") + 1]
    val outName: String = args[args.indexOf("-out") + 1]
    val image = ImageIO.read(File(inName))
    ImageIO.write(image.greyScale(), "png", File(outName))
}