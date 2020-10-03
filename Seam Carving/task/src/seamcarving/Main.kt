package seamcarving

import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val inName = "C:\\Users\\mezen\\IdeaProjects\\Seam Carving\\Seam Carving\\task\\test\\small.png"
    val outName = "C:\\Users\\mezen\\IdeaProjects\\Seam Carving\\Seam Carving\\task\\test\\blu`e.png"
//    val inName = args[args.indexOf("-in") + 1]
//    val outName = args[args.indexOf("-out") + 1]
    val image = ImageIO.read(File(inName))
    createFile(image.greyScale(), outName)
}