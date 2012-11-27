package finder

import java.io.File
import akka.actor.ActorRef

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/25/12
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
class Finder {
  def find(path: File, content: String, listener: ActorRef): Unit = {
    val files = path.listFiles()

    for (file <- files) {
      if (file.isDirectory) find(file, content, listener)
      else grep(file, content, listener)
    }
  }
  def find(path: String, content: String, listener: ActorRef): Unit = {
     find(new File(path), content, listener)
  }
}
