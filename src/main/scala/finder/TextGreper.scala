package finder

import java.io.File
import akka.actor.ActorRef

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/25/12
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */
class TextGreper(listener: ActorRef, matcher: Matcher) extends Greper {


  def grep(file: File, content: String) = {

    val lines = scala.io.Source.fromFile(file).getLines().toList
    val matches = lines.filter(line => matcher(line.trim(), content))

    if (!matches.isEmpty)
      listener ! Result(file.getAbsolutePath, matches)
  }
}
