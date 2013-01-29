package finder

import akka.actor.Actor

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/28/13
 */
class PrintOutListener extends Actor{

  val processor = new ResultPrintOut

  def receive = {
    case result: Result => processor(result)
  }
}
