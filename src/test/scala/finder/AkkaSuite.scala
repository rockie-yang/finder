package finder

import akka.actor.{Props, ActorSystem}
import org.scalatest.FunSuite


/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */
class AkkaSuite extends FunSuite {
  protected val system = ActorSystem("finder")
  protected val listener = system.actorOf(Props[Listener])

}
