package finder

import java.io.File
import akka.actor.ActorRef
import finder.grep

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 12/13/12
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */
class IndexerManagement {
  private def getIndexer(file: File) = {
    val fileName = file.getName
    val ext = fileName.split("\\.").last.toLowerCase

//    val indexer = ext match {
//      case "txt" => new TextIndexer()
//      case "scala" => new TextIndexer(listener, matcher)
//      case _ => new DummyGreper(listener)
//    }

  }
  def find(path: File): Unit = {
    //    println("find in directory " + path.getAbsolutePath)

    val children = path.listFiles()

    val (files, dirs) = children.partition(f => f.isFile)
//
//    files foreach (file => grep(file, content, listener, matcher))
//    dirs foreach (dir => find(dir, content, listener, matcher))

  }


  def index(path: String): Unit = {
//    index(new File(path))
  }
}
