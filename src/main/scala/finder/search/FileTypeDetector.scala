package finder.search

import javax.activation.MimetypesFileTypeMap
import java.io.File

/**
 *
 * there are so many different file types existed
 * So there is really no garanteed way to detect file types.
 *
 * This is used MIME type and file name extention roughly detect file types.
 * If the MIME content type contain certain string,
 * then treat it as text file
 *
 * IANA are registration for MIME types: http://www.iana.org/assignments/media-types
 * Wiki Pedia : http://en.wikipedia.org/wiki/Internet_media_type
 * Some way to detect file types: http://stackoverflow.com/questions/620993/determining-binary-text-file-type-in-java
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/29/13
 */
object FileTypeDetector {
  private val mimeTypesMap = new MimetypesFileTypeMap()

  private val textMimes = List("text", "xml", "html", "jsom")
  private val textExt = List("txt", "c", "cpp", "scala")

  def isTextFile(file: File): Boolean = {
//    println("detecting " + file.getName)
    val mimeType = mimeTypesMap.getContentType(file).toLowerCase
    val ext = file.getName.split("\\.").last

    textMimes.exists(x => mimeType.contains(x)) || textExt.contains(ext)
  }
}
