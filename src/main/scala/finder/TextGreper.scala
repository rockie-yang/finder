package finder

import akka.actor.ActorRef
import java.io.File

/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */

class TextGreper(listener: ActorRef, matcher: Matcher) extends Greper {


  def grep(file: File, content: String) = {

    val lines = scala.io.Source.fromFile(file).getLines().toList
    val matches = lines.filter(line => matcher(line.trim(), content))

    if (!matches.isEmpty)
      listener ! Result(file.getAbsolutePath, matches)
  }
}
