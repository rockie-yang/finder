package finder

import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/25/12
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
class Finder {
  def find(path: File, content: String): Unit = {
    val files = path.listFiles()

    for (file <- files) {
      if (file.isDirectory) find(file, content)
      else grep(file, content)
    }
  }
  def find(path: String, content: String): Unit = {
     find(new File(path), content)
  }
}
