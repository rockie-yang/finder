package finder

import java.io.File
import akka.actor.ActorRef

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/28/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
class DummyGreper(listener: ActorRef)  extends Greper{
  def grep(file: File, content: String) {}
}
