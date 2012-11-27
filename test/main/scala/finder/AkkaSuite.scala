package finder

import org.scalatest.FunSuite
import akka.actor.{Props, ActorSystem}

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/27/12
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
class AkkaSuite extends FunSuite{
  protected val system = ActorSystem("finder")
  protected val listener = system.actorOf(Props[Listener])

}
