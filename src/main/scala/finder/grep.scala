package finder

import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/26/12
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
object grep
{
   def apply(file: File, content: String): List[String] = {
     val fileName = file.getName
     if (fileName.endsWith(".txt")) new TextGreper().grep(file, content)
     else new TextGreper().grep(file, content)
   }

  def apply(path: String, content: String): List[String] = {
    apply(new File(path), content)
  }
}
