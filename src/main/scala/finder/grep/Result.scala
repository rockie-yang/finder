package finder.grep

import java.io.File

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/28/13
 */
case class Result(file: File, matches: List[String]) {
  //  override def toString = fileName + " : " + matches.mkString("\n")
}

trait ResultListener {
  def apply(result: Result)
}

class ResultPrintOutListener extends ResultListener {
  def apply(result: Result) {
    println(result.toString)
  }
}

class ResultHolderListener extends ResultListener {

  var results = List[Result]()

  def apply(result: Result) {
    results = result :: results
  }

}
