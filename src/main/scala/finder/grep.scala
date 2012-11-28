package finder

import java.io.File
import akka.actor.{Actor, ActorRef}

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/26/12
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */

case class Result(fileName: String, matches: List[String])

class Listener extends Actor{
  def receive = {
    case Result(fileName, matches) => {
      println(fileName + " : " + matches.mkString("\n"))
    }
  }
}
object grep
{
   def apply(file: File, content: String, listener: ActorRef, matcher: Matcher): Unit = {
     val fileName = file.getName
     val ext = fileName.split("\\.").last.toLowerCase

     val greper = ext match {
       case "txt" => new TextGreper(listener, matcher)
       case "scala" => new TextGreper(listener, matcher)
       case _ => new DummyGreper(listener)
     }

    greper.grep(file, content)

   }

  def apply(path: String, content: String, listener: ActorRef, matcher: Matcher): Unit = {
    apply(new File(path), content, listener, matcher)
  }
}
