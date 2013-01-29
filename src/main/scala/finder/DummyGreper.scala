package finder

import akka.actor.ActorRef
import java.io.File

/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */
class DummyGreper(listener: ActorRef) extends Greper {
  def grep(file: File) {}
}
