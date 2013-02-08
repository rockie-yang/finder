package finder.search

import java.io.File

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/28/13
 */
case class Result(file: File, matches: List[String]) {
  override def toString = file.getAbsolutePath + "\n    " + matches.mkString("\n    ")
}

trait ResultListener {
  def apply(result: Result)
}

class ResultPrintOutListener(holdResult: Boolean = false) extends ResultListener {
  var results = List[Result]()

  def apply(result: Result) {
    if (holdResult) results = result :: results
    println(result.toString)
  }
}

