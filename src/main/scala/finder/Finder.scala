package finder

import akka.actor.ActorRef
import java.io.File

/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */
object Finder {
  def find(path: File, listener: ActorRef, matcher: Matcher): Unit = {
    //    println("find in directory " + path.getAbsolutePath)

    val children = path.listFiles()

    val (files, dirs) = children.partition(f => f.isFile)

    files foreach (file => grep(file, listener, matcher))
    dirs foreach (dir => find(dir, listener, matcher))

  }


  def find(path: String, listener: ActorRef, matcher: Matcher): Unit = {
    find(new File(path), listener, matcher)
  }
}
