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
object Finder {
  def find(path: File, content: String, listener: ActorRef, matcher: Matcher): Unit = {
//    println("find in directory " + path.getAbsolutePath)

    val children = path.listFiles()

    val (files, dirs) = children.partition(f => f.isFile)

    files foreach (file => grep(file, content, listener, matcher))
    dirs foreach (dir => find(dir, content, listener, matcher))

  }


  def find(path: String, content: String, listener: ActorRef, matcher: Matcher): Unit = {
     find(new File(path), content, listener, matcher)
  }
}
